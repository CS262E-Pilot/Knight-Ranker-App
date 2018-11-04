package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class FollowPOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = FollowPOSTLoader.class.getSimpleName();

    private String follow_sport_id;
    private String follow_player_id;
    private String follow_rank;

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
    public FollowPOSTLoader(@NonNull Context context, String sportID, String playerID, String rank) {
        super(context);

        this.follow_sport_id = sportID;
        this.follow_player_id = playerID;
        this.follow_rank = rank;
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
        Log.e(LOG_TAG, "postFollowInfo called!");
        return FollowNetworkUtils.postFollowInfo(follow_sport_id, follow_player_id, follow_rank);
    }
}
