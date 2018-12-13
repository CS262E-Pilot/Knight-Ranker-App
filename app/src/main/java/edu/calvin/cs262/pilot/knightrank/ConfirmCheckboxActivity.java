package edu.calvin.cs262.pilot.knightrank;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class ConfirmCheckBoxActivity defines an activity that allows resolution of match results
 * between different users by allowing the user to confirm the final scores of the match(es).
 *
 * Note: Is not currently integrated with the back-end and button functionality are placeholder.
 *
 * Note: To the developer of this class, this should be a fragment, not an activity.
 */
public class ConfirmCheckboxActivity extends Fragment {

    //Class variables.
    private static final String LOG_TAG =
            ConfirmCheckboxActivity.class.getSimpleName();

    private ConfirmCheckboxAdapter mListViewDataAdapter;
    private ListView mListViewWithCheckbox;
    private ArrayList<ConfirmItemDTO> mConfirmMatches;

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

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
        return inflater.inflate(R.layout.fragment_confirm_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""));

        // Change the background color to what was selected in color picker.
        // Note: Change color by using findViewById and ID of the UI element you wish to change.
        RelativeLayout thisLayout = getView().findViewById(R.id.fragment_challenge_confirmation_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.WHITE));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        int toolbarColor = mPreferences.getInt(ColorPicker.APP_TOOLBAR_COLOR_ARGB, Color.RED);

        Log.e(LOG_TAG,"Value of color is: " + value);

        // Get listview checkbox.
        mListViewWithCheckbox = (ListView) getView().findViewById(R.id.list_view_with_checkbox);

        // When list view item is clicked.
        mListViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                ConfirmItemDTO itemDto = (ConfirmItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked()) {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                } else {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }
            }
        });

        // Click this button to reverse select listview items.
        Button invertButton = (Button) getView().findViewById(R.id.list_select);
        invertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mConfirmMatches.size();
                for(int i=0;i<size;i++)
                {
                    ConfirmItemDTO dto = mConfirmMatches.get(i);

                    if(dto.isChecked()) {
                        dto.setChecked(false);
                    } else {
                        dto.setChecked(true);
                    }
                }
                mListViewDataAdapter.notifyDataSetChanged();
            }
        });

        // Click this button to remove selected items from listview.
        Button rejectButton = (Button) getView().findViewById(R.id.list_reject);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Are you sure to reject the selected match results?");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = mConfirmMatches.size();
                        for(int i=0;i<size;i++) {
                            ConfirmItemDTO dto = mConfirmMatches.get(i);

                            if(dto.isChecked()) {
                                mConfirmMatches.remove(i);
                                i--;
                                size = mConfirmMatches.size();
                            }
                        }
                        mListViewDataAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
            }
        });

        Button confirmButton = (Button) getView().findViewById(R.id.list_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setMessage("Are you sure to confirm the selected match results?");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = mConfirmMatches.size();
                        for(int i=0;i<size;i++) {
                            ConfirmItemDTO dto = mConfirmMatches.get(i);


                            if(dto.isChecked()) {
                                final int index = i;
                                MatchNetworkUtils.confirmMatch(getContext(), dto.getId(), new MatchNetworkUtils.PUTConfirmMatchResponse() {
                                    @Override
                                    public void onResponse(String message) {

                                        mConfirmMatches.remove(index);
                                    }
                                });
                            }
                        }
                        mListViewDataAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
            }
        });

        // Load the matches
        loadConfirmMatches();
    }

    private void loadConfirmMatches() {
        MatchNetworkUtils.getConfirmMatches(getContext(), new MatchNetworkUtils.GETMatchesResponse() {
            @Override
            public void onResponse(ArrayList<ConfirmItemDTO> confirmMatches) {
                mConfirmMatches = confirmMatches;
                // Create a custom list view adapter with checkbox control.
                mListViewDataAdapter = new ConfirmCheckboxAdapter(getContext(), confirmMatches);

                mListViewDataAdapter.notifyDataSetChanged();

                // Set data adapter to list view.
                mListViewWithCheckbox.setAdapter(mListViewDataAdapter);
            }
        });
    }
}