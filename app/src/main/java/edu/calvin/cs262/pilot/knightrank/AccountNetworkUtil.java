package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

/**
 * Class AccountNetworkUtil defines the methods and functions necessary to communicate with the
 * RESTFul web service to retrieve information from the PostgreSQL database for Google OAuth
 * back-end font-end functionality.
 */
public class AccountNetworkUtil {

    /** Callback interface for delivering sports request. */
    public interface POSTTokenResponse {
        void onResponse(String token);
    }

    private static final String LOG_TAG = AccountNetworkUtil.class.getSimpleName();
    private static final String LOGIN_URL = "https://calvin-cs262-fall2018-pilot.appspot.com/knightranker/v1/login";

    /**
     * POST
     * and adds to Volley request queue.
     */
    void postToken(final Context context, String idToken, final AccountNetworkUtil.POSTTokenResponse res) {
        Log.d(LOG_TAG, "token:" + idToken);
        JSONObject data = new JSONObject();
        try {
            data.put("idToken", idToken);
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Failed to add token to request");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            res.onResponse(response.getString("token"));
                        } catch (JSONException e) {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, error.toString());
                Toast.makeText(context, "Failed to login", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
