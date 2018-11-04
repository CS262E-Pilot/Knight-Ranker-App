package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class FollowDELETELoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = FollowDELETELoader.class.getSimpleName();

    private String follow_data_entry_id;

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
    public FollowDELETELoader(@NonNull Context context, String data_entry_id) {
        super(context);

        this.follow_data_entry_id = data_entry_id;
    }


    /**
     * Method performs task in background.
     *
     * @return query results.
     */
    @Nullable
    @Override
    public String loadInBackground() {

        // Call method to POST to the specified URI.
        Log.e(LOG_TAG, "deleteFollowInfo called!");
        return FollowNetworkUtils.deleteFollowInfo(follow_data_entry_id);
    }
}
