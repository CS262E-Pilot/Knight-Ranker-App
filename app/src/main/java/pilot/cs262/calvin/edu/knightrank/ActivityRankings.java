package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ActivityRankings extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            ActivityRankings.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_rankings);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Custom icon for the Up button
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // Set-up for the drawer navigation.
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Method call to setup drawer view.
        setupDrawerContent(navigationView);

        // Placeholder - in case we want to do something with drawer states.
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
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
     * @param item
     * @return
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
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    /**
     * Method that controls what happens when we select a navigation drawer item.
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;

        // To handle selection of menu items.
        switch (menuItem.getItemId()) {
            case R.id.nav_activity_selection:
                Log.e(LOG_TAG, "Reaches here?");
                fragmentClass = ActivitySelection.class;
                Intent intent1 = new Intent(getApplicationContext(), ActivitySelection.class);
                startActivity(intent1);
                Log.e(LOG_TAG, "Selected the activity selection activity!");
                //Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
                return;
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

//        // Check that the activity is using the layout version with
//        // the fragment_container FrameLayout
//        if (findViewById(R.id.fragment_content) != null) {
//
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return false;
//            }
//        }

        // Create a new Fragment.
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        try {
            fragment.setArguments(getIntent().getExtras());
        } catch (Exception e){
            e.printStackTrace();
        }

        // Necessary for fragments.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_content, fragment).commit();
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }
}
