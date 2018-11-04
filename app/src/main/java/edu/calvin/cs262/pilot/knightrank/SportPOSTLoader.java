package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class SportPOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = SportPOSTLoader.class.getSimpleName();

    private String sport_name;
    private String sport_type;

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
    public SportPOSTLoader(@NonNull Context context, String name, String type) {
        super(context);

        this.sport_name = name;
        this.sport_type = type;
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
        Log.e(LOG_TAG, "postSportInfo called!");
        return SportNetworkUtils.postSportInfo(sport_name, sport_type);
    }
}
