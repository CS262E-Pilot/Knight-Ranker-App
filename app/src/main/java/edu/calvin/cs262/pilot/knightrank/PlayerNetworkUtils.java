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

/**
 * Meta class that currently handles all the connections to the Knight-Ranker WebService Endpoints.
 *
 * Note: Need to refactor into separate .java files otherwise it's going to get hellishly long.
 */
public class PlayerNetworkUtils {

    private static final String LOG_TAG = PlayerNetworkUtils.class.getSimpleName();

    // Player URI for Knight-Ranker Database Player Table.
    private static final String PLAYER_LIST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/players";
    private static final String PLAYER_ID_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/player/";
    private static final String PLAYER_POST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/player";
    private static final String PLAYER_DELETE_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/player/";
    private static final String PLAYER_PUT_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/player/";

    /**
     * Method puts to the specified URI.
     *
     * @param id player id
     * @param email player email address
     * @param account_creation_date player account creation date
     * @return
     */

    public static String putPlayerInfo(String id, String email, String account_creation_date) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(PLAYER_PUT_URL).buildUpon()
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
            jsonObject.accumulate("emailAddress", email);
            jsonObject.accumulate("accountCreationDate",  account_creation_date);

            // Create stream to output the data.
            OutputStream os = urlConnection.getOutputStream();

            // Create writer to write to the stream to output the data.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Write the data.
            writer.write(jsonObject.toString());

            // Write to log.e what we're trying to send.
            Log.e(PlayerNetworkUtils.class.toString(), jsonObject.toString());

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
     * @param email email of the player
     * @param account_creation_date account creation date of the player
     * @return String indicating success or failure
     */
    static String postPlayerInfo(String email, String account_creation_date) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(PLAYER_POST_URL).buildUpon()

                    .build();

            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            // Define connection and request.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // Define the data we wish to send.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("emailAddress", email);
            jsonObject.accumulate("accountCreationDate",  account_creation_date);

            // Create stream to output the data.
            OutputStream os = urlConnection.getOutputStream();

            // Create writer to write to the stream to output the data.
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            // Write the data.
            writer.write(jsonObject.toString());

            // Write to log.e what we're trying to send.
            Log.e(PlayerNetworkUtils.class.toString(), jsonObject.toString());

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
    static String getPlayerListInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String playerListJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(PLAYER_LIST_URL).buildUpon()

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
            playerListJSONString = buffer.toString();

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
            if(playerListJSONString != null) {
                Log.e(LOG_TAG, playerListJSONString);
                return playerListJSONString;
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
    static String getPlayerIDInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String playerIDJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(PLAYER_ID_URL).buildUpon()
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
            playerIDJSONString = buffer.toString();

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
            if(playerIDJSONString != null) {
                Log.e(LOG_TAG, playerIDJSONString);
                return playerIDJSONString;
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
     * @param player_data_entry_id player id - primary key
     * @return the results of the request
     */
    public static String deletePlayerInfo(String player_data_entry_id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(PLAYER_DELETE_URL).buildUpon()
                    .appendPath(player_data_entry_id)
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
