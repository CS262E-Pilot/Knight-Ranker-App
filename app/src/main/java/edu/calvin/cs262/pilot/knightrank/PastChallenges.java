package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * Class PastChallenges defines a Fragment that displays the previous matches in various sports
 */
public class PastChallenges extends Fragment implements AdapterView.OnItemSelectedListener {

    //Class variables.
    private static final String LOG_TAG = PastChallenges.class.getSimpleName();

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    private ListView mPastChallenges;
    private Spinner mSportSpinner;

    private ArrayList<PastMatch> pastMatches = new ArrayList<>();
    private PastMatchAdapter pastMatchAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set shared preferences component.
        // Note: modified from the one in activities as this is a fragment.
        mPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        mPreferencesDefault = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_history, container, false);
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
        RelativeLayout thisLayout = Objects.requireNonNull(getView()).findViewById(R.id.fragment_recent_challenges_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.WHITE));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        // Setup the sport spinner
        Set<String> selectedSports = getActivity().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).getStringSet(getString(R.string.selected_sports), null);
        mSportSpinner = getView().findViewById(R.id.past_challenges_spinner);
        if(selectedSports != null) {
            List<String> selected_sports_arraylist = new ArrayList<String>(selectedSports);
            ArrayAdapter<String> arrayAdapterActivities = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    selected_sports_arraylist);
            mSportSpinner.setAdapter(arrayAdapterActivities);
        }

        pastMatchAdapter = new PastMatchAdapter(getActivity(), pastMatches);
        mPastChallenges = getView().findViewById(R.id.past_challenges_listview);
        mPastChallenges.setAdapter(pastMatchAdapter);
        mSportSpinner.setOnItemSelectedListener(this);

        Log.e(LOG_TAG,"Value of color is: " + value);
    }

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        loadPastMatches(parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void loadPastMatches(String sport) {
        new MatchNetworkUtils().getPastMatches(getContext(), sport, new MatchNetworkUtils.GETMatchResponse() {
            @Override
            public void onResponse(ArrayList<PastMatch> result) {
                setPastMatches(result);
            }
        });
    }

    private void setPastMatches(ArrayList<PastMatch> pastMatchItems) {
        Log.d(LOG_TAG, "Past Matches: " + pastMatchItems.toString());
        pastMatches.clear();
        pastMatches.addAll(pastMatchItems);
        pastMatchAdapter.notifyDataSetChanged();
    }
}
