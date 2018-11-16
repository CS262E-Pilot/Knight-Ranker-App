package edu.calvin.cs262.pilot.knightrank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.calvin.cs262.pilot.knightrank.R;

public class SportSelection extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Private class members.
    private static final String LOG_TAG = SportSelection.class.getSimpleName();

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";


    private ListView mSportListView;
    private FloatingActionButton mFab;

    private String[] sports = new String[]{};
    private ArrayAdapter<String> sportAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_selection);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // We are hiding the action bar since it isn't relevant at this stage
        if (ab != null) {
            ab.hide();
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Set shared preferences component.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(this);


        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");

        // Find component(s).
        mFab = findViewById(R.id.fab);
        // Make the fab invisible until we have some sports checked
        mFab.setVisibility(View.INVISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view);
            }
        });

        // Loads Sport Relation entries from the Knight Ranker Database.
        mSportListView = findViewById(R.id.sport_list);
        loadSports();

        // Change the background color to what was selected in color picker.
        // Note: Change color by using findViewById and ID of the UI element you wish to change.
        CoordinatorLayout thisLayout = findViewById(R.id.activity_sport_selection_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.YELLOW));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        int toolbarColor = mPreferences.getInt(ColorPicker.APP_TOOLBAR_COLOR_ARGB, Color.YELLOW);

        // Change the toolbar color to what was selected in color picker.
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(toolbarColor));

        Log.e(LOG_TAG, "Value of color is: " + value);
    }

    /**
     * Method obtains list of sports from the database.
     */
    private void loadSports() {
        new SportNetworkUtils().getSports(this, new SportNetworkUtils.GETSportsResponse() {
            @Override
            public void onResponse(ArrayList<Sport> result) {
                setSports(result);
            }
        });
    }

    /**
     * Method populates the sport selection list with sport activities.
     *
     * @param result list of all sport activities.
     */
    private void setSports(ArrayList<Sport> result) {
        ArrayList<String> sportNamesList = new ArrayList<>();
        for (Sport sport : result) {
            sportNamesList.add(sport.getName());
        }
        sports = sportNamesList.toArray(new String[0]);
        sportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, sports);
        mSportListView.setAdapter(sportAdapter);
        mSportListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSportListView.setOnItemClickListener(this);
        // Check any sports we have already selected from our preferences
        Set<String> selectedSportsSet = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).getStringSet(getString(R.string.selected_sports), null);
        if (selectedSportsSet != null) {
            mFab.setVisibility(View.VISIBLE);
            List<String> selectedSports = new ArrayList<>(selectedSportsSet);
            for (String sport : selectedSports) {
                mSportListView.setItemChecked(Arrays.asList(sports).indexOf(sport), true);
            }
        }
    }

    /**
     * Method starts the ActivityRankings.java activity upon user button click.
     *
     * @param view View object.
     */
    public void startActivity(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).edit();
        // Add all our checked sports into the shared preferences so we can reference them later
        SparseBooleanArray checked = mSportListView.getCheckedItemPositions();
        Set<String> selectedSports = new HashSet<>();
        for (int i = 0; i < mSportListView.getAdapter().getCount(); i++) {
            // If we have a check, show the button and stop looping
            if (checked.get(i)) {
                selectedSports.add(sports[i]);
            }
        }
        editor.putStringSet(getString(R.string.selected_sports), selectedSports).apply();

        // Start our new intent
        Intent intent = new Intent(this, ActivityRankings.class);
        startActivity(intent);
    }

    /**
     * Method controls events once user selects a sport.
     *
     * @param adapter AdapterView object
     * @param arg1    View object
     * @param arg2    integer value
     * @param arg3    long value.
     */
    @Override
    public void onItemClick(AdapterView<?> adapter, View arg1, int arg2, long arg3) {
        // Loop over the checked array and show the continue button if we have at least one check
        SparseBooleanArray checked = mSportListView.getCheckedItemPositions();
        for (int i = 0; i < mSportListView.getAdapter().getCount(); i++) {
            // If we have a check, show the button and stop looping
            if (checked.get(i)) {
                mFab.setVisibility(View.VISIBLE);
                return;
            }
        }
        // We haven't returned so that means we have no checks, so hide the continue button
        mFab.setVisibility(View.INVISIBLE);
    }

    /**
     * Method adds an options menu to the toolbar.
     *
     * @param menu menu object
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method to control what happens when menu items are selected.
     *
     * @param item the item selected
     * @return whatever
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent1 = new Intent(this, SettingsActivity.class);
                startActivity(intent1);
                return true;
            case R.id.color_picker:
                Intent intent2 = new Intent(this, ColorPicker.class);
                startActivity(intent2);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

}
