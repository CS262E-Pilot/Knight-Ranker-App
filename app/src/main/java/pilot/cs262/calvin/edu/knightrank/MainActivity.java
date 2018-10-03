package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    // Launches team rankings activity upon button click.
    public void launchActivity(View view) {

        Intent intent = new Intent(this, ActivityRankings.class);

        startActivity(intent);

        Log.d(LOG_TAG, "Button clicked!");
    }

    // Launches account login activity upon button click.
    public void launchActivity2(View view) {

        Intent intent = new Intent(this, AccountCreation.class);

        startActivity(intent);

        Log.d(LOG_TAG, "Button clicked!");
    }
}
