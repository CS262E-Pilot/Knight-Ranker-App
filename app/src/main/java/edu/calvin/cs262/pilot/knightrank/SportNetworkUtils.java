package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SportNetworkUtils {

    /** Callback interface for delivering sports request. */
    public interface GetSportsResponse {
        void onResponse(ArrayList<Sport> sports);
    }

    private static final String LOG_TAG = SportNetworkUtils.class.getSimpleName();

    // Sport URI for Knight-Ranker Database Sport Table.
    private static final String SPORT_LIST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sports";
    private static final String SPORT_ID_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport/";
    private static final String SPORT_POST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport";
    private static final String SPORT_DELETE_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport/";
    private static final String SPORT_PUT_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport/";

    private static final String SPORT_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sports";
    /**
     * Gets all the sports
     *
     * @return a list of sports
     */
    void getSports(final Context context, final GetSportsResponse res) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SPORT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the results
                            ArrayList<Sport> sports = new ArrayList<>();
                            JSONArray sportsJSONArray = response.getJSONArray("items");
                            // Create an ArrayList of sports
                            for (int i = 0; i < sportsJSONArray.length(); i++) {
                                JSONObject sportJSON = (JSONObject) sportsJSONArray.get(i);
                                sports.add(new Sport(sportJSON.getInt("id"), sportJSON.getString("name"), sportJSON.getString("type")));
                            }
                            // Return the resulting sport array list
                            res.onResponse(sports);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed to load Sports", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
    /**
     * Method puts to the specified URI.
     *
     * @param id sport id
     * @param name sport name
     * @param type sport type
     * @return
     */
    public static String putSportInfo(String id, String name, String type) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(SPORT_POST_URL).buildUpon()
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
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("type",  type);

            // Create stream to output the data.
            OutputStream os = urlConnection.getOutputStream();

            // Create writer to write to the stream to output the data.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Write the data.
            writer.write(jsonObject.toString());

            // Write to log.e what we're trying to send.
            Log.e(SportNetworkUtils.class.toString(), jsonObject.toString());

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
     * Method posts to the specified URI.
     *
     * @param name name of the sport
     * @param type type of the sport
     * @return String indicating success or failure
     */
    static String postSportInfo(String name, String type) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(SPORT_POST_URL).buildUpon()

                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Define connection and request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // Define the data we wish to send.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("type",  type);

            // Create stream to output the data.
            OutputStream os = urlConnection.getOutputStream();

            // Create writer to write to the stream to output the data.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Write the data.
            writer.write(jsonObject.toString());

            // Write to log.e what we're trying to send.
            Log.e(SportNetworkUtils.class.toString(), jsonObject.toString());

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
    static String getSportListInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String sportListJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(SPORT_LIST_URL).buildUpon()

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
            sportListJSONString = buffer.toString();

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
            if (sportListJSONString != null) {
                Log.e(LOG_TAG, sportListJSONString);
                return sportListJSONString;
            } else {
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
    static String getSportIDInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String sportIDJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(SPORT_ID_URL).buildUpon()
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
            sportIDJSONString = buffer.toString();

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
            if (sportIDJSONString != null) {
                Log.e(LOG_TAG, sportIDJSONString);
                return sportIDJSONString;
            } else {
                return "";
            }
        }
    }

    /**
     * Method deletes the data entry in the table specified by the ID.
     * TODO: Not functional yet.
     *
     * @param sport_data_entry_id sport id - primary key
     * @return the results of the request
     */
    public static String deleteSportInfo(String sport_data_entry_id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(SPORT_DELETE_URL).buildUpon()
                    .appendPath(sport_data_entry_id)
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
