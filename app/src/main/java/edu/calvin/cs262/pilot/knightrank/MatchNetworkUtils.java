package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Class MatchNetworkUtils defines the methods and functions necessary to communicate with the
 * RESTFul web service to retrieve information from the PostgreSQL database for Match endpoint
 * back-end front-end functionality.
 */
public class MatchNetworkUtils {

    private static final String LOG_TAG = MatchNetworkUtils.class.getSimpleName();

    // Match URI for Knight-Ranker Database Match Table.
    private static final String MATCH_POST_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/match";
    private static final String UNCONFIRMED_MATCH_GET_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/matches";
    /** Callback interface for delivering sports request. */
    public interface POSTMatchResponse {
        void onResponse(String message);
    }

    public interface GETMatchesResponse {
        void onResponse(ArrayList<ConfirmItemDTO> confirmMatches);
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
}
