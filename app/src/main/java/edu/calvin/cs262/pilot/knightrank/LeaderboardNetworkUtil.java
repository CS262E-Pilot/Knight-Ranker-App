package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.net.Uri;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardNetworkUtil {

    /** Callback interface for delivering sports request. */
    public interface GETLeaderboardResponse {
        void onResponse(ArrayList<Player> players);
    }

    private static final String LOG_TAG = LeaderboardNetworkUtil.class.getSimpleName();
    private static final String SPORT_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/rankings";

    /**
     * GETS all the sports in JSON format, creates new Sport objects (Sport.java), adds to ArrayList<Sport>,
     * and adds to Volley request queue.
     */
    void getLeaderboard(final Context context, String sport, final GETLeaderboardResponse res) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SPORT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the results
                            ArrayList<Player> players = new ArrayList<>();
                            JSONArray sportsJSONArray = response.getJSONArray("items");
                            // Create an ArrayList of sports
                            for (int i = 0; i < sportsJSONArray.length(); i++) {
                                JSONObject sportJSON = (JSONObject) sportsJSONArray.get(i);
                                players.add(new Player(sportJSON.getInt("id"), sportJSON.getString("name"), sportJSON.getInt("rank")));
                            }
                            // Return the resulting sport array list
                            res.onResponse(players);
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
}
