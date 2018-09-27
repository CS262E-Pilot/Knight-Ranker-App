package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class TeamRankings extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            TeamRankings.class.getSimpleName();

    private TextView mTextMessage;

    // Set-up for bottom navigation bar.
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ActivitySelection:
                    mTextMessage.setText(R.string.title_activity_selection);

                    // Launches the activity upon selection.
                    Intent intent1 = new Intent(getApplicationContext(), ActivitySelection.class);

                    startActivity(intent1);

                    Log.d(LOG_TAG, "Bottom Bar Navigation Item Selected");

                    return true;
                case R.id.MyRankings:
                    mTextMessage.setText(R.string.title_my_rankings);

                    // Launches the activity upon selection.
                    Intent intent2 = new Intent(getApplicationContext(), MyRankings.class);

                    startActivity(intent2);

                    Log.d(LOG_TAG, "Bottom Bar Navigation Item Selected");

                    return true;
                case R.id.Challenge:
                    mTextMessage.setText(R.string.title_challenge);

                    // Launches the activity upon selection.
                    Intent intent3 = new Intent(getApplicationContext(), ChallengeResults.class);

                    startActivity(intent3);

                    Log.d(LOG_TAG, "Bottom Bar Navigation Item Selected");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_rankings);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Bottom Navigation Bar
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
