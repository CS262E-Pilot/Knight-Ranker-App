package edu.calvin.cs262.pilot.knightrank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

public class TestPOSTPUTPlayerBackEnd extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // Private class members.
    private static final String LOG_TAG = TestPOSTPUTSportBackEnd.class.getSimpleName();

    private EditText editTextViewPlayerID;
    private EditText editTextViewPlayerEmail;
    private EditText editTextViewPlayerAccountCreationDate;
    private TextView textViewNetworkStatus;
    private TextView textViewRequestStatus;

    private String playerIDString;
    private String playerEmailString;
    private String playerAccountCreationDateString;

    private String whichCRUDIsIt;

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_postplayer_back_end);

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

        // Find components.
        editTextViewPlayerID = findViewById(R.id.input_player_put_id);
        editTextViewPlayerEmail = findViewById(R.id.input_player_email);
        editTextViewPlayerAccountCreationDate = findViewById(R.id.input_player_account_creation_date);
        textViewNetworkStatus = findViewById(R.id.network_connected);
        textViewRequestStatus = findViewById(R.id.request_status);

        // Reconnect to the loader if one exists already, upon device config change.
        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        // Change the background color to what was selected in color picker.
        // Note: Change color by using findViewById and ID of the UI element you wish to change.
        RelativeLayout thisLayout = findViewById(R.id.activity_test_player_post_back_end_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.YELLOW));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        int toolbarColor = mPreferences.getInt(ColorPicker.APP_TOOLBAR_COLOR_ARGB, Color.YELLOW);

        // Change the toolbar color to what was selected in color picker.
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(toolbarColor));

        Log.e(LOG_TAG,"Value of color is: " + value);
    }

    /**
     * Method begins Google Cloud endpoint put process upon button click.
     *
     * @param view view component
     */
    public void put(View view) {

        whichCRUDIsIt = "put";

        // Get the sport input strings.
        playerIDString = editTextViewPlayerID.getText().toString();
        playerEmailString = editTextViewPlayerEmail.getText().toString();
        playerAccountCreationDateString = editTextViewPlayerAccountCreationDate.getText().toString();

        if(playerIDString.length() == 0){
            playerIDString = "1";
        }
        if(playerEmailString.length() == 0){
            playerEmailString = "default player email";
        }
        if(playerAccountCreationDateString.length() == 0){
            playerAccountCreationDateString = "2018-11-03 00:53:57.048546";
        }

        // Close keyboard after hitting search query button.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Initialize network info components.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;

        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // Check connection is available, we are connected.
        if (networkInfo != null && networkInfo.isConnected()) {

            // show "Connected" & type of network "WIFI or MOBILE"
            textViewNetworkStatus.setText("Connected "+networkInfo.getTypeName());
            // change background color to red
            textViewNetworkStatus.setBackgroundColor(0xFF7CCC26);

            // Refactored to user AsyncTaskLoader via PlayerGETLoader.java
            Bundle postBundle = new Bundle();
            postBundle.putString("player_id", playerIDString);
            postBundle.putString("player_email", playerEmailString);
            postBundle.putString("player_account_creation_date", playerAccountCreationDateString);
            getSupportLoaderManager().restartLoader(0, postBundle,this);

            // Indicate to user query is in process.
            textViewRequestStatus.setText("");
            textViewRequestStatus.setText("PUT in-progress");
        } else {

            // show "Not Connected"
            textViewNetworkStatus.setText("Not Connected");
            // change background color to green
            textViewNetworkStatus.setBackgroundColor(0xFFFF0000);

            // There is no available connection.
            textViewRequestStatus.setText("");
            textViewRequestStatus.setText(R.string.no_connection);
        }
    }

    /**
     * Method begins Google Cloud endpoint post process upon button click.
     *
     * @param view view component
     */
    public void post(View view) {

        whichCRUDIsIt = "post";

        // Get the sport input strings.
        playerEmailString = editTextViewPlayerEmail.getText().toString();
        playerAccountCreationDateString = editTextViewPlayerAccountCreationDate.getText().toString();

        if(playerEmailString.length() == 0){
            playerEmailString = "default player email";
        }
        if(playerAccountCreationDateString.length() == 0){
            playerAccountCreationDateString = "2018-11-03 00:53:57.048546";
        }

        // Close keyboard after hitting search query button.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Initialize network info components.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;

        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // Check connection is available, we are connected.
        if (networkInfo != null && networkInfo.isConnected()) {

            // show "Connected" & type of network "WIFI or MOBILE"
            textViewNetworkStatus.setText("Connected "+networkInfo.getTypeName());
            // change background color to red
            textViewNetworkStatus.setBackgroundColor(0xFF7CCC26);

            // Refactored to user AsyncTaskLoader via PlayerGETLoader.java
            Bundle postBundle = new Bundle();
            postBundle.putString("player_email", playerEmailString);
            postBundle.putString("player_account_creation_date", playerAccountCreationDateString);
            getSupportLoaderManager().restartLoader(0, postBundle,this);

            // Indicate to user query is in process.
            textViewRequestStatus.setText("");
            textViewRequestStatus.setText("POST in-progress");
        } else {

            // show "Not Connected"
            textViewNetworkStatus.setText("Not Connected");
            // change background color to green
            textViewNetworkStatus.setBackgroundColor(0xFFFF0000);

            // There is no available connection.
            textViewRequestStatus.setText("");
            textViewRequestStatus.setText(R.string.no_connection);
        }
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
     * Method called when load is instantiated.
     *
     * NOTE: change the Loader.java class if we want to obtain different data.
     *
     * @param id
     * @param bundle contains data
     * @return query results
     */
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {

        if (whichCRUDIsIt.contains("put")) {
            return new PlayerPUTLoader(this, bundle.getString("player_id"),bundle.getString("player_email"), bundle.getString("player_account_creation_date"));
        }
        else if (whichCRUDIsIt.contains("post")) {
            return new PlayerPOSTLoader(this, bundle.getString("player_email"), bundle.getString("player_account_creation_date"));
        }
        return null;
    }

    /**
     * Method called when loader task is finished.  Add code to update UI with results.
     *
     * NOTE: modify as necessary according to the data we obtained.
     *
     * @param loader loader object.
     * @param s
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {

        if (whichCRUDIsIt.contains("put")) {
            // Method call to test player PUT.
            TestPlayerPUTBackend(s);
        }
        if (whichCRUDIsIt.contains("post")) {
            // Method call to test match POST.
            TestPlayerPOSTBackend(s);
        }
    }

    /**
     * Method to test whether we can successfully post to the database.
     *
     * @param s response message from the RESTful web service.
     */
    private void TestPlayerPUTBackend(String s) {

        // POST the response we get to the TextView.
        textViewRequestStatus.setText("Response from RESTful web service\n");
        textViewRequestStatus.append("OK = success, anything else = BAD\n");
        textViewRequestStatus.append("Result:");
        textViewRequestStatus.append(s);
    }

    /**
     * Method to test whether we can successfully post to the database.
     *
     * @param s response message from the RESTful web service.
     */
    private void TestPlayerPOSTBackend(String s) {

        // POST the response we get to the TextView.
        textViewRequestStatus.setText("Response from RESTful web service\n");
        textViewRequestStatus.append("OK = success, anything else = BAD\n");
        textViewRequestStatus.append("Result:");
        textViewRequestStatus.append(s);
    }

    /**
     * Method called to clean up any remaining resources.
     *
     * @param loader loader object.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
