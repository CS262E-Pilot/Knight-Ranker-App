package pilot.cs262.calvin.edu.knightrank;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AccountCreation extends AppCompatActivity {

    //Class variables.
    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    private EditText username;
    private EditText password;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        username = (EditText) findViewById(R.id.editText_username);
        password = (EditText) findViewById(R.id.editText_password);
        confirmPassword = (EditText) findViewById(R.id.editText_confirm_Password);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
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
}
