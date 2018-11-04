package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PlayerPOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PlayerPOSTLoader.class.getSimpleName();

    private String player_email;
    private String player_account_creation_date;

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
    public PlayerPOSTLoader(@NonNull Context context, String email, String account_creation_date) {
        super(context);

        this.player_email = email;
        this.player_account_creation_date = account_creation_date;
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
        Log.e(LOG_TAG, "postPlayerInfo called!");
        return PlayerNetworkUtils.postPlayerInfo(player_email, player_account_creation_date);
    }
}
