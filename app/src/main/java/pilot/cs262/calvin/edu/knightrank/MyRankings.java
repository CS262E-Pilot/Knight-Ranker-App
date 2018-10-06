package pilot.cs262.calvin.edu.knightrank;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class MyRankings extends Fragment {

    // Class variables.

    // For use with shared preferences.
    private static String PLACEHOLDER6 = "";

    // Share preferences file.
    private SharedPreferences mPreferences;
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set shared preferences component.
        // Note: modified from the one in activities as this is a fragment.
        mPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        //mPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_rankings, container, false);
    }

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor8 = mPreferences.edit();

        preferencesEditor8.putString(PLACEHOLDER6, "Placeholder text 6");

        preferencesEditor8.apply();
    }
}
