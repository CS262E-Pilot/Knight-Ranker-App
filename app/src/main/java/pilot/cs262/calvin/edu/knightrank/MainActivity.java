package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    private EditText username_main;
    private EditText password_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        username_main = (EditText) findViewById(R.id.editText_username_main);
        password_main = (EditText) findViewById(R.id.editText_password_main);

    }

    /**
     * Method for button click that launches the activity.
     *
     * @param view as stated
     */
    public void launchActivity(View view) {
        String entered_username = username_main.getText().toString();
        String entered_password = password_main.getText().toString();

        if(!entered_username.equals("")) {
            if(!entered_password.equals("")) {
                Intent intent = new Intent(this, ActivityRankings.class);

                startActivity(intent);

                Log.d(LOG_TAG, "Button clicked!");
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a username.", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Method for button click that launches the activity.
     *
     * @param view as stated
     */
    public void launchActivity2(View view) {

        Intent intent = new Intent(this, AccountCreation.class);

        startActivity(intent);

        Log.d(LOG_TAG, "Button clicked!");
    }
}
