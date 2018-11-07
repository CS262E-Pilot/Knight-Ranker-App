package edu.calvin.cs262.pilot.knightrank;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewChallenges.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewChallenges#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewChallenges extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Class variables.
    private static final String LOG_TAG = NewChallenges.class.getSimpleName();

    // For use with shared preferences.
    private static final String PLACEHOLDER3 = "";

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner mSpinner;

    private OnFragmentInteractionListener mListener;

    public NewChallenges() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewChallenges.
     */
    // TODO: Rename and change types and number of parameters
    public static NewChallenges newInstance(String param1, String param2) {
        NewChallenges fragment = new NewChallenges();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Set shared preferences component.
        // Note: modified from the one in activities as this is a fragment.
        mPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
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
        View view = inflater.inflate(R.layout.fragment_new_challenge, container, false);
        mSpinner = view.findViewById(R.id.sport_spinner);
        return view;
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
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.YELLOW));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        Log.e(LOG_TAG,"Value of color is: " + value);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Context will be defined because of lifecycle
        Context context = getContext();
        // Return early if context is null to make type checking happy
        if (context == null) {
            return;
        }
        // Setup the sport spinner
        // Specify the layout to use when the list of choices appears
        Set<String> sports = context.getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).getStringSet(getString(R.string.selected_sports), new HashSet<String>());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, sports.toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
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

        SharedPreferences.Editor preferencesEditor5 = mPreferences.edit();

        preferencesEditor5.putString(PLACEHOLDER3, "Placeholder text 3");

        preferencesEditor5.apply();
    }
}
