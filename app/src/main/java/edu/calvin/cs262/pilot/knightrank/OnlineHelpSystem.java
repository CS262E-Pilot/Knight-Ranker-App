package edu.calvin.cs262.pilot.knightrank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Class OnlineHelpSystem defines an Activity that hosts various Fragments that presents to the user
 * technical instructions on how to navigate and perform certain action in the Knight-Ranker application.
 */
public class OnlineHelpSystem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AccountCreationInstructions.OnFragmentInteractionListener, AccountLoginInstructions.OnFragmentInteractionListener,
        MatchCreationInstructions.OnFragmentInteractionListener, MatchConfirmationInstructions.OnFragmentInteractionListener {

    //Class variables.
    private static final String LOG_TAG =
            OnlineHelpSystem.class.getSimpleName();

    // For use with shared preferences.
    private static final String PLACEHOLDERHHHELP = "placeholder_help";

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_help_system);

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
        LinearLayout thisLayout = findViewById(R.id.activity_online_help_system_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.WHITE));

        int toolbarColor = mPreferences.getInt(ColorPicker.APP_TOOLBAR_COLOR_ARGB, Color.RED);

        // Change the toolbar color to what was selected in color picker.
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(toolbarColor));

        // Find drawer components.
        mDrawerLayout = findViewById(R.id.help_drawer_layout);
        NavigationView navigationView = findViewById(R.id.help_nav_view);

        // Method call to setup drawer view.
        navigationView.setNavigationItemSelectedListener(this);

        // Test that we can retrieve color value from Color picker.
        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);
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
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that controls what happens when we select a navigation drawer item.
     *
     * @param menuItem the item selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_account_creation_instructions:
                fragment = new AccountCreationInstructions();
                break;
            case R.id.nav_account_login_instructions:
                fragment = new AccountLoginInstructions();
                break;
            case R.id.nav_match_creation_instructions:
                fragment = new MatchCreationInstructions();
                break;
            case R.id.nav_match_confirmation_instructions:
                fragment = new MatchConfirmationInstructions();
                break;
            case R.id.nav_exit_help:
                Intent intent1 = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(intent1);
                return true;
            default:
                fragment = new AccountCreationInstructions();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.help_fragment_content, fragment);
        ft.commit();

        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawer(GravityCompat.START);
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

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        preferencesEditor.putString(PLACEHOLDERHHHELP, "Placeholder help text");

        preferencesEditor.apply();
    }
}
