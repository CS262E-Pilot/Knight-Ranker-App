package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.net.Uri;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

public class MatchNetworkUtils {

    private static final String LOG_TAG = MatchNetworkUtils.class.getSimpleName();

    // Match URI for Knight-Ranker Database Match Table.
    private static final String MATCH_LIST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/matches";
    private static final String MATCH_ID_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/match/";
    private static final String MATCH_POST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/match";
    private static final String MATCH_DELETE_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/match/";
    private static final String MATCH_PUT_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/match/";

    private static final String MATCHES_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/matches";


    public interface GETMatchResponse {
        void onResponse(ArrayList<Match> matches);
    }
    /**
     * GETs the matches for a given player
     * @param context
     * @param res
     */
    void getUnconfirmedMatches(final Context context, final MatchNetworkUtils.GETMatchResponse res) {
        StringBuilder query = new StringBuilder();
        query.append(MATCHES_URL);
        query.append("/?matches=");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, query.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the results
                            ArrayList<Match> unconfirmedMatches = new ArrayList<>();
                            JSONArray unconfirmedMatchJSONArray = response.getJSONArray("items");
                            // Create an ArrayList of to be confirmed matches for a player
                            for (int i = 0; i < unconfirmedMatchJSONArray.length(); i++) {
                                JSONObject unconfirmedMatchJSON = (JSONObject) unconfirmedMatchJSONArray.get(i);
                                if(!unconfirmedMatchJSON.getString("verified").equals("t")) {
                                    if(unconfirmedMatchJSON.getInt("playerOneID") == 5 || unconfirmedMatchJSON.getInt("playerOneID") == 6)
                                    unconfirmedMatches.add(new Match(unconfirmedMatchJSON.getInt("id"),
                                            unconfirmedMatchJSON.getString("time"),
                                            unconfirmedMatchJSON.getString("verified"),
                                            unconfirmedMatchJSON.getInt("winner"),
                                            unconfirmedMatchJSON.getInt("playerOneScore"),
                                            unconfirmedMatchJSON.getInt("playerTwoScore"),
                                            unconfirmedMatchJSON.getInt("playerOneID"),
                                            unconfirmedMatchJSON.getInt("playerTwoID"),
                                            unconfirmedMatchJSON.getInt("sportID")));
                                }
                            }
                            // Return the resulting sport array list
                            res.onResponse(unconfirmedMatches);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed to load Leaderboard", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    /**
     *  Method puts to the specified URI.
     *
     * @param id match id
     * @param sport_id match sport id
     * @param player_one_id match player one id
     * @param player_two_id match player two id
     * @param player_one_score match player one score
     * @param player_two_score match player two score
     * @param winner match winner (player)
     * @param timestamp match timestamp
     * @param verified whether match is verified
     * @return String indicating success or failure
     */
    static String putMatchInfo(String id, String sport_id, String player_one_id, String player_two_id,
                                String player_one_score, String player_two_score, String winner, String timestamp, String verified){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(MATCH_PUT_URL).buildUpon()
                    .appendPath(id)
                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Define connection and request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // Define the data we wish to send.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("sportID", sport_id);
            jsonObject.accumulate("playerOneID",  player_one_id);
            jsonObject.accumulate("playerTwoID",  player_two_id);
            jsonObject.accumulate("playerOneScore",  player_one_score);
            jsonObject.accumulate("playerTwoScore",  player_two_score);
            jsonObject.accumulate("winner",  winner);
            jsonObject.accumulate("time",  timestamp);
            jsonObject.accumulate("verified",  verified);

            // Create stream to output the data.
            OutputStream os = urlConnection.getOutputStream();

            // Create writer to write to the stream to output the data.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Write the data.
            writer.write(jsonObject.toString());

            // Write to log.e what we're trying to send.
            Log.e(MatchNetworkUtils.class.toString(), jsonObject.toString());

            // Close resources.
            writer.flush();
            writer.close();
            os.close();

            // Initiate the connection and attempt to post.
            urlConnection.connect();

        } catch (Exception ex) {

            ex.printStackTrace();
            return "PUT failed!";
        } finally {
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch(Exception ex){
                ex.printStackTrace();
                return "PUT failed!";
            }
        }
    }

    /**
     *  Method posts to the specified URI.
     *
     * @param sport_id match sport id
     * @param player_one_id match player one id
     * @param player_two_id match player two id
     * @param player_one_score match player one score
     * @param player_two_score match player two score
     * @param winner match winner (player)
     * @param timestamp match timestamp
     * @param verified whether match is verified
     * @return String indicating success or failure
     */
    static String postMatchInfo(String sport_id, String player_one_id, String player_two_id,
                                String player_one_score, String player_two_score, String winner, String timestamp, String verified)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(MATCH_POST_URL).buildUpon()

                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Define connection and request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // Define the data we wish to send.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("sportID", sport_id);
            jsonObject.accumulate("playerOneID",  player_one_id);
            jsonObject.accumulate("playerTwoID",  player_two_id);
            jsonObject.accumulate("playerOneScore",  player_one_score);
            jsonObject.accumulate("playerTwoScore",  player_two_score);
            jsonObject.accumulate("winner",  winner);
            jsonObject.accumulate("time",  timestamp);
            jsonObject.accumulate("verified",  verified);

            // Create stream to output the data.
            OutputStream os = urlConnection.getOutputStream();

            // Create writer to write to the stream to output the data.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Write the data.
            writer.write(jsonObject.toString());

            // Write to log.e what we're trying to send.
            Log.e(MatchNetworkUtils.class.toString(), jsonObject.toString());

            // Close resources.
            writer.flush();
            writer.close();
            os.close();

            // Initiate the connection and attempt to post.
            urlConnection.connect();

        } catch (Exception ex) {

            ex.printStackTrace();
            return "POST failed!";
        } finally {
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch(Exception ex){
                ex.printStackTrace();
                return "POST failed!";
            }
        }
    }

    /**
     * Method queries specified URI.
     *
     * @param queryString what we're searching for
     * @return the results
     */
    static String getMatchListInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String matchListJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(MATCH_LIST_URL).buildUpon()

                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Open connection and make request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the response.
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
           /* Since it's JSON, adding a newline isn't necessary (it won't affect
              parsing) but it does make debugging a *lot* easier if you print out the
              completed buffer for debugging. */
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            matchListJSONString = buffer.toString();

        } catch (Exception ex) {

            ex.printStackTrace();
            return "Connection failed!";
        } finally {

            // Close all connections opened.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(matchListJSONString != null) {
                Log.e(LOG_TAG, matchListJSONString);
                return matchListJSONString;
            }
            else{
                return "";
            }
        }
    }

    /**
     * Method queries specified URI.
     *
     * @param queryString what we're searching for
     * @return the results
     */
    static String getMatchIDInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String matchIDJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(MATCH_ID_URL).buildUpon()
                    .appendPath(queryString)
                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Open connection and make request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the response.
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
           /* Since it's JSON, adding a newline isn't necessary (it won't affect
              parsing) but it does make debugging a *lot* easier if you print out the
              completed buffer for debugging. */
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            matchIDJSONString = buffer.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Connection failed!";
        } finally {

            // Close all connections opened.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(matchIDJSONString != null) {
                Log.e(LOG_TAG, matchIDJSONString);
                return matchIDJSONString;
            }
            else{
                return "";
            }
        }
    }

    /**
     * Method deletes the data entry in the table specified by the ID.
     * TODO: Not functional yet.
     *
     * @param match_data_entry_id match id - primary key
     * @return the results of the request
     */
    public static String deleteMatchInfo(String match_data_entry_id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(MATCH_DELETE_URL).buildUpon()
                    .appendPath(match_data_entry_id)
                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Open connection and make request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.connect();

            // Read the response. (don't remove, otherwise DELETE WILL FAIL)
            // Don't ask me why that is....it just is.....
            InputStream inputStream = urlConnection.getInputStream();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Connection failed!";
        } finally {

            // Close all connections opened.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch(Exception ex){
                ex.printStackTrace();
                return "DELETE failed!";
            }
        }
    }
}
