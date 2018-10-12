package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ActivityRankings extends AppCompatActivity
        implements NewChallenges.OnFragmentInteractionListener, RecentChallenges.OnFragmentInteractionListener {

    //Class variables.
    private static final String LOG_TAG =
            ActivityRankings.class.getSimpleName();

    // For use with shared preferences.
    private static final String PLACEHOLDER1 = "placeholder1";

    private DrawerLayout mDrawerLayout;

    // Share preferences file.
    private SharedPreferences mPreferences;
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
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
        setupDrawerContent(navigationView, savedInstanceState);

        // Method call to manipulate drawer states.
        navDrawerStates();

        // Set shared preferences component.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        //mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""));

        Log.e(LOG_TAG, "Reaches end of onCreate?");

    }

    /**
     * Method to execute functions according to navigation drawer states.
     * Note: Placeholder - can be removed if we don't want to do anything in the future.
     */
    private void navDrawerStates() {
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
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
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Log.e(LOG_TAG, "Reaches onNavigationItemSelected?");
                        selectDrawerItem(menuItem, savedInstanceState);
                        return true;
                    }
                });
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
            case R.id.nav_main_activity:
                fragmentClass = MainActivity.class;
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                Log.e(LOG_TAG, "Selected the main activity!");
                Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
                return true;
            //break;
            case R.id.nav_activity_selection:
                fragmentClass = ActivitySelection.class;
                Intent intent2 = new Intent(getApplicationContext(), ActivitySelection.class);
                startActivity(intent2);
                Log.e(LOG_TAG, "Selected the activity selection activity!");
                Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
                return true;
            //break;
            case R.id.nav_new_challenges:
                fragmentClass = NewChallenges.class;
                Log.e(LOG_TAG, "Selected the new challenges fragment!");
                break;
            case R.id.nav_recent_challenges:
                fragmentClass = RecentChallenges.class;
                Log.e(LOG_TAG, "Selected the recent challenges fragment!");
                break;
            case R.id.nav_my_rankings:
                fragmentClass = MyRankings.class;
                Log.e(LOG_TAG, "Selected the my rankings fragment!");
                break;
            case R.id.nav_challenge_results:
                fragmentClass = ChallengeResults.class;
                Log.e(LOG_TAG, "Selected the challenge results fragment!");
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

        Log.e(LOG_TAG, "Reaches end of selectDrawerItem?");
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
