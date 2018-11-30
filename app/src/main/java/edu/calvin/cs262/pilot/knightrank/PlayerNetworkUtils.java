package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Class PlayerNetworkUtils defines the methods and functions necessary to communicate with the
 * RESTFul web service to retrieve information from the PostgreSQL database for Player endpoint
 * back-end front-end functionality.
 */
public class PlayerNetworkUtils {

    private static final String LOG_TAG = PlayerNetworkUtils.class.getSimpleName();

    // Player URI for Knight-Ranker Database Player Table.
    private static final String PLAYER_LIST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/players";

    /** Callback interface for delivering sports request. */
    public interface GETPlayersResponse {
        void onResponse(ArrayList<Player> players);
    }
    /**
     * GET
     * gets a list of all the players
     * @param context
     * @param res
     */
    static void getPlayers(final Context context, final PlayerNetworkUtils.GETPlayersResponse res) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PLAYER_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the results
                            ArrayList<Player> players = new ArrayList<>();
                            JSONArray playerRankJSONArray = response.getJSONArray("items");
                            // Create an ArrayList of players
                            for (int i = 0; i < playerRankJSONArray.length(); i++) {
                                JSONObject playerRankJSON = (JSONObject) playerRankJSONArray.get(i);
                                players.add(new Player(playerRankJSON.getInt("id"), playerRankJSON.getString("name"), playerRankJSON.getString("emailAddress")));
                            }
                            // Return the resulting players list
                            res.onResponse(players);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Failed to load Players", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
