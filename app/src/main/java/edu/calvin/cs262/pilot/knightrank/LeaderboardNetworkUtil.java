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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LeaderboardNetworkUtil {

    /** Callback interface for delivering sports request. */
    public interface GETLeaderboardResponse {
        void onResponse(ArrayList<PlayerRank> playerRanks);
    }

    private static final String LOG_TAG = LeaderboardNetworkUtil.class.getSimpleName();
    private static final String LEADERBOARD_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/leaderboard";

    /**
     * GETs the leaderboard for a given sport
     * @param context
     * @param sport
     * @param res
     */
    void getLeaderboard(final Context context, String sport, final GETLeaderboardResponse res) {
        StringBuilder query = new StringBuilder();
        query.append(LEADERBOARD_URL);
        query.append("/?sport=");
        try {
            query.append(URLEncoder.encode(sport, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context, "Can't parse sport name", Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, query.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the results
                            ArrayList<PlayerRank> playerRanks = new ArrayList<>();
                            JSONArray playerRankJSONArray = response.getJSONArray("items");
                            // Create an ArrayList of sports
                            for (int i = 0; i < playerRankJSONArray.length(); i++) {
                                JSONObject playerRankJSON = (JSONObject) playerRankJSONArray.get(i);
                                playerRanks.add(new PlayerRank(playerRankJSON.getInt("eloRank"), playerRankJSON.getString("name")));
                            }
                            // Return the resulting sport array list
                            res.onResponse(playerRanks);
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
