package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PlayerGETLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PlayerGETLoader.class.getSimpleName();

    private String mQueryString;

    /**
     * Method called when starting the loader.
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        // Starts the loadinBackground() method.
        forceLoad();
    }

    /**
     * Constructor
     *
     * @param context application context.
     */
    public PlayerGETLoader(@NonNull Context context, String query) {
        super(context);

        this.mQueryString = query;
    }


    /**
     * Method performs task in background.
     *
     * @return query results.
     */
    @Nullable
    @Override
    public String loadInBackground() {

        // Call method to query specified URI.
        // Based on whether string is empty or contains a positive integer value.
        if(mQueryString.length() == 0){
            Log.e(LOG_TAG, "getPlayerListInfo called!");
            return PlayerNetworkUtils.getPlayerListInfo(mQueryString);
        }
        else {
            Log.e(LOG_TAG, "getPlayerIDInfo called!");
            return PlayerNetworkUtils.getPlayerIDInfo(mQueryString);
        }
    }
}
