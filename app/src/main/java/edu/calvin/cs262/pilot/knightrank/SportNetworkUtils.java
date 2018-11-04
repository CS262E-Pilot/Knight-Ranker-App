package edu.calvin.cs262.pilot.knightrank;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class SportNetworkUtils {

    private static final String LOG_TAG = SportNetworkUtils.class.getSimpleName();

    // Sport URI for Knight-Ranker Database Sport Table.
    private static final String SPORT_LIST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sports";
    private static final String SPORT_ID_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport/";
    private static final String SPORT_POST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport";
    private static final String SPORT_DELETE_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/sport/";

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

                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Define connection and request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // Define the data we wish to send.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ID", sport_data_entry_id);

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
            return "DELETE failed!";
        } finally {
            try {
                return Objects.requireNonNull(urlConnection).getResponseMessage() + "";
            } catch(Exception ex){
                ex.printStackTrace();
                return "DELETE failed!";
            }
        }
    }
}
