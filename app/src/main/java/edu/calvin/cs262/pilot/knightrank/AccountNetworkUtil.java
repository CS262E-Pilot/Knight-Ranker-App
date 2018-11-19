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

import java.util.ArrayList;

public class AccountNetworkUtil {

    /** Callback interface for delivering sports request. */
    public interface POSTTokenResponse {
        void onResponse(Player player);
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
        Log.d(LOG_TAG, data.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, response.toString());
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
