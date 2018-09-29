package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Toast;

public class TeamRankings extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            TeamRankings.class.getSimpleName();

    private TextView mTextMessage;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_rankings);

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        // Create a new fragment and specify the fragment to show based on nav item clicked
                        Fragment fragment = null;
                        Class fragmentClass;

                        // To handle selection of menu items.
                        switch (menuItem.getItemId()) {
//                            case R.id.nav_activity_selection:
//                                fragmentClass = ActivitySelection.class;
//                                Log.e(LOG_TAG, "Selected the activity selection activity!");
//                                break;
//                            case R.id.nav_team_rankings:
//                                fragmentClass = TeamRankings.class;
//                                Log.e(LOG_TAG, "Selected the team rankings activity!");
//                                break;
                            case R.id.nav_my_rankings:
                                fragmentClass = MyRankings.class;
                                Log.e(LOG_TAG, "Selected the my rankings fragment!");
                                break;
                            case R.id.nav_challenge_results:
                                fragmentClass = ChallengeResults.class;
                                Log.e(LOG_TAG, "Selected the challenge results fragment!");
                                //Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
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
                        return true;
                    }
                });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
