package pilot.cs262.calvin.edu.knightrank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String USER_NAME = "";
    private static final String USER_PASSWORD = "";

    private EditText username_main;
    private EditText password_main;

    // Share preferences file.
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // User login components.
        username_main = findViewById(R.id.editText_username_main);
        password_main = findViewById(R.id.editText_password_main);

        // Set shared preferences component.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Restores user name and user password from saved preferences file.
        username_main.setText(mPreferences.getString(USER_NAME, ""));
        password_main.setText(mPreferences.getString(USER_PASSWORD, ""));
    }

    /**
     * Method for button click corresponding to user login.
     *
     * @param view as stated
     */
    public void launchActivity(View view) {
        String entered_username = username_main.getText().toString();
        String entered_password = password_main.getText().toString();

        // User login checks and conditions.
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

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        preferencesEditor.putString(USER_NAME, username_main.getText().toString());
        preferencesEditor.putString(USER_PASSWORD, username_main.getText().toString());

        preferencesEditor.apply();
    }

    /**
     * Method resets the shared preferences file if we desire to.
     *
     * @param view view component
     */
    public void resetSharedPreferences(View view){

        // Clear preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
}
