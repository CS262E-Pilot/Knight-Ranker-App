package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
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

/**
 * Class MatchNetworkUtils defines the methods and functions necessary to communicate with the
 * RESTFul web service to retrieve information from the PostgreSQL database for PastMatch endpoint
 * back-end front-end functionality.
 */
public class MatchNetworkUtils {

    private static final String LOG_TAG = MatchNetworkUtils.class.getSimpleName();

    // PastMatch URI for Knight-Ranker Database PastMatch Table.
    private static final String MATCH_POST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/match";
    private static final String MATCH_GET_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/matches";

    /** Callback interface for delivering sports request. */
    public interface POSTMatchResponse {
        void onResponse(String message);
    }

    /** Callback interface for delivering sports request. */
    public interface GETMatchResponse {
        void onResponse(ArrayList<PastMatch> matches);
    }

    /**
     * Record a match and send it to the server so it can be verified by opponent
     * @param context
     * @param opponentID the id of the desired opponent
     * @param playerScore the score of the player
     * @param opponentScore the score of the opponent
     * @param res
     */
    static void postMatch(final Context context, String sport, int opponentID, int playerScore, int opponentScore, final MatchNetworkUtils.POSTMatchResponse res) {
        // Build the request object
        JSONObject data = new JSONObject();
        try {
            data.put("token", AccountNetworkUtil.getToken(context));
            data.put("sport", sport);
            data.put("opponentID", opponentID);
            data.put("playerScore", playerScore);
            data.put("opponentScore", opponentScore);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Failed to build request object");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MATCH_POST_URL, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            res.onResponse(response.getString("message"));
                        } catch (JSONException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, error.toString());
                Toast.makeText(context, "Failed to record match", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    static void getPastMatches(final Context context, final String sport, final GETMatchResponse res) {
        StringBuilder query = new StringBuilder();
        query.append(MATCH_GET_URL);
        query.append("/confirm");
        query.append("/?token=");
        try {
            query.append(URLEncoder.encode(AccountNetworkUtil.getToken(context), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context, "Bad token", Toast.LENGTH_LONG).show();
        }
        query.append("&sport=");
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
                            ArrayList<PastMatch> pastMatches = new ArrayList<>();
                            JSONArray pastMatchJSONArray = response.getJSONArray("items");
                            // Create an ArrayList of past matches for a sport
                            for (int i = 0; i < pastMatchJSONArray.length(); i++) {
                                JSONObject pastMatchJSON = (JSONObject) pastMatchJSONArray.get(i);
                                pastMatches.add(new PastMatch(pastMatchJSON.getInt("id"),
                                        pastMatchJSON.getString("time"),
                                        pastMatchJSON.getInt("playerScore"),
                                        pastMatchJSON.getInt("opponentScore"),
                                        pastMatchJSON.getString("playerName"),
                                        pastMatchJSON.getString("opponentName")));
                            }

                            // Return the resulting sport array list
                            res.onResponse(pastMatches);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed to load pastMatches", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
