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
 * Class PastChallenges defines a Fragment that displays the previous challenges the user
 * has participated in.
 */
public class PastChallenges extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    //Class variables.
    private static final String LOG_TAG =
            PastChallenges.class.getSimpleName();

    // For use with shared preferences.
    private static String PLACEHOLDER4 = "";

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



    private OnFragmentInteractionListener mListener;

    public PastChallenges() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PastChallenges.
     */
    // TODO: Rename and change types and number of parameters
    public static PastChallenges newInstance(String param1, String param2) {
        PastChallenges fragment = new PastChallenges();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        mPastChallenges = (ListView) getView().findViewById(R.id.past_challenges_listview);
        mPastChallenges.setAdapter(pastMatchAdapter);

        Log.e(LOG_TAG,"Value of color is: " + value);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

        SharedPreferences.Editor preferencesEditor6 = mPreferences.edit();

        preferencesEditor6.putString(PLACEHOLDER4, "Placeholder text 4");

        preferencesEditor6.apply();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        loadPastMatches(parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void loadPastMatches(String sport) {
        /*
        new SportNetworkUtils().getSportNameInfo(getContext(), sport, new SportNetworkUtils.GETSportResponse() {
            @Override
            public void onResponse(int sportID) {

            }
        });
        */
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
