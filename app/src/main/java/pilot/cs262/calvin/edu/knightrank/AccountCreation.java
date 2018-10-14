package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AccountCreation extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    private static final String USER_NAME_CREATION = "user name creation";
    private static final String USER_PASSWORD_CREATION = "user password creation";
    private static final String CONFIRM_PASSWORD = "confirm password";

    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Find UI components.
        username = findViewById(R.id.editText_username_create);
        password = findViewById(R.id.editText_password_create);
        confirmPassword = findViewById(R.id.editText_confirm_Password_create);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Set shared preferences component.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(this);

        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");

        // Restores user name and user password from saved preferences file.
        username.setText(mPreferences.getString(USER_NAME_CREATION, "User name"));
        password.setText(mPreferences.getString(USER_PASSWORD_CREATION, "User password"));
        confirmPassword.setText(mPreferences.getString(CONFIRM_PASSWORD, "User password"));
    }

    /**
     * Method for button click that launches the activity.
     *
     * @param view as stated
     */
    public void launchActivity(View view) {

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        Log.d(LOG_TAG, "Button clicked!");
    }

    /**
     * Method performs checks and conditions on user account creation.
     * - Valid user name (can't be an empty string)
     * - Password match confirmation
     *
     * @param view view component
     */
    public void createAccount(View view) {
        String user_name = username.getText().toString();
        String user_password = password.getText().toString();
        String user_confirmed_password = confirmPassword.getText().toString();

        if(!user_name.equals("")) {
            if(user_password.equals(user_confirmed_password) && !user_password.equals("")) {
                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);

                Log.d(LOG_TAG, "Button clicked!");
                Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Passwords must match.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Username must not be blank.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method currently called to store values to shared preferences file.
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor2 = mPreferences.edit();

        preferencesEditor2.putString(USER_NAME_CREATION, username.getText().toString());
        preferencesEditor2.putString(USER_PASSWORD_CREATION, password.getText().toString());
        preferencesEditor2.putString(CONFIRM_PASSWORD, confirmPassword.getText().toString());

        preferencesEditor2.apply();
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }
}
