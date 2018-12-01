package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * Class DeclareMath defines a Fragment that permits a user to challenge another user to a match
 * in a particular sport activity.
 */
public class RecordMatch extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //Class variables.
    private static final String LOG_TAG = RecordMatch.class.getSimpleName();

    // For use with shared preferences.
    private static final String PLACEHOLDER3 = "";

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    private String sport;
    private ArrayList<Player> players;
    private AutoCompleteTextView mAutoCompleteTextView;
    private TextInputLayout mPlayerScoreTextInput;
    private TextInputLayout mOpponentScoreTextInput;
    private OnFragmentInteractionListener mListener;

    public RecordMatch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordMatch.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordMatch newInstance(String param1, String param2) {
        RecordMatch fragment = new RecordMatch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set shared preferences component.
        // Note: modified from the one in activities as this is a fragment.
        mPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mPreferencesDefault = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""))
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_match, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        RelativeLayout thisLayout = Objects.requireNonNull(getView()).findViewById(R.id.fragment_new_challenges_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.WHITE));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        Log.e(LOG_TAG,"Value of color is: " + value);

        Set<String> selectedSports = getActivity().getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).getStringSet(getString(R.string.selected_sports), null);
        Spinner activitySpinner = getView().findViewById(R.id.activity_spinner);
        if(selectedSports != null) {
            List<String> selected_sports_arraylist = new ArrayList<String>(selectedSports);
            ArrayAdapter<String> arrayAdapterActivities = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    selected_sports_arraylist);
            activitySpinner.setAdapter(arrayAdapterActivities);
        }
        activitySpinner.setOnItemSelectedListener(this);

        mAutoCompleteTextView = getView().findViewById(R.id.search_bar);
        loadPlayers();

        // Set out click listener for the submit button
        Button submitButton = getView().findViewById(R.id.submit_match);
        submitButton.setOnClickListener(this);

        mPlayerScoreTextInput = getView().findViewById(R.id.player_score);
        mOpponentScoreTextInput = getView().findViewById(R.id.opponent_score);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.submit_match:
                submitMatch();
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        sport = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Submit the match to the server
     */
    private void submitMatch() {
        String opponentName = mAutoCompleteTextView.getText().toString();
        Player opponent = null;
        for (Player p : players) {
            if (p.getName().equals(opponentName)) {
                opponent = p;
            }
        }
        if (opponent != null) {
            try {
                int playerScore = Integer.parseInt(mPlayerScoreTextInput.getEditText().getText().toString());
                int opponentScore = Integer.parseInt(mOpponentScoreTextInput.getEditText().getText().toString());
                MatchNetworkUtils.postMatch(getContext(), sport, opponent.getId(), playerScore, opponentScore, new MatchNetworkUtils.POSTMatchResponse() {
                    @Override
                    public void onResponse(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (NullPointerException e) {
                Toast.makeText(getContext(), "Invalid score", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Load players and add them to the autocompletetext so that they can be selected
     */
    private void loadPlayers() {
        PlayerNetworkUtils.getPlayers(getContext(), new PlayerNetworkUtils.GETPlayersResponse() {
            @Override
            public void onResponse(ArrayList<Player> players) {
                setPlayers(players);
            }
        });
    }

    /**
     * Creates a list of names for the AutoCompleteTextView adapter
     * @param result
     */
    private void setPlayers(ArrayList<Player> result) {
        // Set a reference to our result so we can use it later
        players = result;
        ArrayList<String> playerNameList = new ArrayList<>();
        for (Player player : result) {
            playerNameList.add(player.getName());
        }
        String[] players = playerNameList.toArray(new String[0]);
        ArrayAdapter sportAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, players);
        mAutoCompleteTextView.setAdapter(sportAdapter);
    }
}
