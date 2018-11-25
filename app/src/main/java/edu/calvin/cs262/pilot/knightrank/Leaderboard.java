package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class Leaderboard extends Fragment implements AdapterView.OnItemSelectedListener{

    //Class variables.
    private static final String LOG_TAG = Leaderboard.class.getSimpleName();

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    private ListView mListViewLeaderboard;
    private Spinner mSportSpinner;

    private ArrayList<PlayerRank> playerRankItems = new ArrayList<>();
    private PlayerRankAdapter playerRankAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set shared preferences component.
        // Note: modified from the one in activities as this is a fragment.
        mPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        mPreferencesDefault = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    /**
     * Method implemented to change background color programmatically via color picket.
     *
     * @param view object
     * @param savedInstanceState object
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Change the background color to what was selected in color picker.
        // Note: Change color by using findViewById and ID of the UI element you wish to change.
        LinearLayout thisLayout = Objects.requireNonNull(getView()).findViewById(R.id.fragment_leaderboard_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.WHITE));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        Log.e(LOG_TAG,"Value of color is: " + value);

        // Set our adapter
        playerRankAdapter = new PlayerRankAdapter(getActivity(), playerRankItems);
        // Setup the sport spinner
        Set<String> selectedSports = getActivity().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).getStringSet(getString(R.string.selected_sports), null);
        mSportSpinner = getView().findViewById(R.id.leaderboard_sport_spinner);
        if(selectedSports != null) {
            List<String> selected_sports_arraylist = new ArrayList<String>(selectedSports);
            ArrayAdapter<String> arrayAdapterActivities = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    selected_sports_arraylist);
            mSportSpinner.setAdapter(arrayAdapterActivities);
        }

        mListViewLeaderboard = getView().findViewById(R.id.leaderboard_listview);
        mListViewLeaderboard.setAdapter(playerRankAdapter);
        mSportSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        loadLeaderboard(parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void loadLeaderboard(String sport) {
        new LeaderboardNetworkUtil().getLeaderboard(getContext(), sport, new LeaderboardNetworkUtil.GETLeaderboardResponse() {
            @Override
            public void onResponse(ArrayList<PlayerRank> result) {
                setLeaderboard(result);
            }
        });
    }

    private void setLeaderboard(ArrayList<PlayerRank> playerRanks) {
        Log.d(LOG_TAG, "PlayerRanks: " + playerRanks.toString());
        playerRankItems.clear();
        playerRankItems.addAll(playerRanks);
        playerRankAdapter.notifyDataSetChanged();
    }
}
