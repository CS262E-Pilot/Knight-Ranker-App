package edu.calvin.cs262.pilot.knightrank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityRankings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NewChallenge.OnFragmentInteractionListener,
        PastChallenges.OnFragmentInteractionListener {

    //Class variables.
    private static final String LOG_TAG =
            ActivityRankings.class.getSimpleName();

    // For use with shared preferences.
    private static final String PLACEHOLDER1 = "placeholder1";

    private DrawerLayout mDrawerLayout;

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    private Spinner mActivitySpinner;
    private ListView mActivityRankings;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_content, new Leaderboard());
        ft.commit();

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Custom icon for the Up button
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        // Find drawer components.
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Method call to setup drawer view.
        navigationView.setNavigationItemSelectedListener(this);

        // Set shared preferences component.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(this);

        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""));

        // Change the background color to what was selected in color picker.
        // Note: Change color by using findViewById and ID of the UI element you wish to change.
        LinearLayout thisLayout = findViewById(R.id.fragment_activity_rankings_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.WHITE));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        int toolbarColor = mPreferences.getInt(ColorPicker.APP_TOOLBAR_COLOR_ARGB, Color.RED);

        // Change the toolbar color to what was selected in color picker.
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(toolbarColor));

        Log.e(LOG_TAG,"Value of color is: " + value);
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
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.settings:
                Intent intent1 = new Intent(this, SettingsActivity.class);
                startActivity(intent1);
                return true;
            case R.id.color_picker:
                Intent intent2 = new Intent(this, ColorPicker.class);
                startActivity(intent2);
                return true;
            case R.id.online_help_system:
                Intent intent3 = new Intent(this, OnlineHelpSystem.class);
                startActivity(intent3);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to set-up the navigation drawer.
     *
     * @param navigationView navigation drawer .xml code in activity_rankings * @param savedInstanceState necessary for conditional checks before implementing fragment switch
     */
    private void setupDrawerContent(NavigationView navigationView, final Bundle savedInstanceState) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem, savedInstanceState);
                        return true;
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_activity_selection:
                fragment = new Leaderboard();
                break;
            case R.id.nav_new_challenge:
                fragment = new NewChallenge();
                break;
            case R.id.nav_sport_selection:
                Intent intent4 = new Intent(getApplicationContext(), SportSelection.class);
                startActivity(intent4);
                return true;
            case R.id.nav_challenge_confirmation:
                fragment = new ChallengeConfirmation();
                break;
            case R.id.nav_past_challenges:
                fragment = new PastChallenges();
                break;
            default:
                fragment = new Leaderboard();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        ft.commit();

        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method that controls what happens when we select a navigation drawer item.
     *
     * @param menuItem the item selected
     * @param savedInstanceState necessary for conditional checks before implementing fragment switch
     */
    public boolean selectDrawerItem(MenuItem menuItem, Bundle savedInstanceState) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;

        // To handle selection of menu items.
        switch (menuItem.getItemId()) {
            case R.id.nav_activity_selection:
                fragmentClass = Leaderboard.class;
                break;
            case R.id.nav_sport_selection:
                fragmentClass = SportSelection.class;
                Intent intent4 = new Intent(getApplicationContext(), SportSelection.class);
                startActivity(intent4);
                Log.e(LOG_TAG, "Selected the sport selection activity!");
                Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_new_challenge:
                fragmentClass = NewChallenge.class;
                break;
            case R.id.nav_challenge_confirmation:
                fragmentClass = ChallengeConfirmation.class;
                break;
            case R.id.nav_past_challenges:
                fragmentClass = PastChallenges.class;
                break;
            default:
                fragmentClass = ActivitySelection.class;
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_content) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return false;
            }
        }

        // Create a new Fragment.
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        if (fragment != null) {
            fragment.setArguments(getIntent().getExtras());
        }


        // Necessary for fragments.
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back,
        // and finally commit to the transaction.
        fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).addToBackStack(null).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
        return true;
    }

    /**
     * Method for default Host Activity - Fragment communication
     * Note: Can be removed as long as we remove the corresponding OnFragmentInteractionListener
     * method in the specific fragment itself.
     *
     * @param uri the data you want sent/received
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor3 = mPreferences.edit();

        preferencesEditor3.putString(PLACEHOLDER1, "Placeholder text 1");

        preferencesEditor3.apply();
    }
}
