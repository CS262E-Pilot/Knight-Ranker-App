package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class MatchPOSTLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = MatchPOSTLoader.class.getSimpleName();

    private String match_sport_id;
    private String match_player_one_id;
    private String match_player_two_id;
    private String match_player_one_score;
    private String match_player_two_score;
    private String match_winner;
    private String match_timestamp;
    private String match_verified;

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
    public MatchPOSTLoader(@NonNull Context context, String sport_id, String player_one_id, String player_two_id,
                           String player_one_score, String player_two_score, String winner, String timestamp, String verified) {
        super(context);

        this.match_sport_id = sport_id;
        this.match_player_one_id = player_one_id;
        this.match_player_two_id = player_two_id;
        this.match_player_one_score = player_one_score;
        this.match_player_two_score = player_two_score;
        this.match_winner = winner;
        this.match_timestamp = timestamp;
        this.match_verified = verified;
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
        Log.e(LOG_TAG, "postMatchInfo called!");
        return MatchNetworkUtils.postMatchInfo(match_sport_id, match_player_one_id, match_player_two_id,
                match_player_one_score, match_player_two_score, match_winner, match_timestamp, match_verified);
    }
}
