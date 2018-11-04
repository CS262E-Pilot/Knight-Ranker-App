package edu.calvin.cs262.pilot.knightrank;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FollowNetworkUtils
{
    private static final String LOG_TAG = FollowNetworkUtils.class.getSimpleName();

    // Follow URI for Knight-Ranker Database Follow Table.
    private static final String FOLLOW_LIST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/follows";
    private static final String FOLLOW_ID_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/follow/";

    /**
     * Method queries specified URI.
     *
     * @param queryString what we're searching for
     * @return the results
     */
    static String getFollowListInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String followListJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(FOLLOW_LIST_URL).buildUpon()

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
            followListJSONString = buffer.toString();

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
            if(followListJSONString != null) {
                Log.e(LOG_TAG, followListJSONString);
                return followListJSONString;
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
    static String getFollowIDInfo(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Stores results.
        String followIDJSONString = null;

        try {
            //Build up your query URI.
            Uri builtURI = Uri.parse(FOLLOW_ID_URL).buildUpon()
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
            followIDJSONString = buffer.toString();

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
            if(followIDJSONString != null) {
                Log.e(LOG_TAG, followIDJSONString);
                return followIDJSONString;
            }
            else{
                return "";
            }
        }
    }
}
