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
import android.support.constraint.ConstraintLayout;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class TestBackEnd extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // Private class members.
    private static final String LOG_TAG = TestBackEnd.class.getSimpleName();

    private EditText editTextQueryString;
    private TextView textViewSearchResults;

    private String queryString;

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_back_end);

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
        editTextQueryString = findViewById(R.id.search_string);
        textViewSearchResults = findViewById(R.id.search_results);

        // Reconnect to the loader if one exists already, upon device config change.
        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        queryString = "";

        // Change the background color to what was selected in color picker.
        // Note: Change color by using findViewById and ID of the UI element you wish to change.
        ConstraintLayout thisLayout = findViewById(R.id.activity_test_back_end_root_layout);
        thisLayout.setBackgroundColor(mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.YELLOW));

        int value = mPreferences.getInt(ColorPicker.APP_BACKGROUND_COLOR_ARGB, Color.BLACK);

        int toolbarColor = mPreferences.getInt(ColorPicker.APP_TOOLBAR_COLOR_ARGB, Color.YELLOW);

        // Change the toolbar color to what was selected in color picker.
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(toolbarColor));

        Log.e(LOG_TAG,"Value of color is: " + value);
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
     * Method begins Google Cloud endpoint query process upon button click.
     *
     * @param view view component
     */
    public void fetch(View view) {

        // Get the query string.
        queryString = editTextQueryString.getText().toString();

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

        // Check connection is available, we are connected, and query string is not empty.
        if (networkInfo != null && networkInfo.isConnected()) {

            // Refactored to user AsyncTaskLoader via PlayerLoader.java
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle,this);

            // Indicate to user query is in process.
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.loading_in_process);
        } else {
            // There is no available connection.
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_connection);
        }
    }

    /**
     * Method called when load is instantiated.
     *
     * NOTE: change the Loader.java class if we want to obtain different data.
     *
     * @param i
     * @param bundle contains data
     * @return query results
     */
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        //return new PlayerLoader(this, Objects.requireNonNull(bundle).getString("queryString"));
        //return new SportLoader(this, Objects.requireNonNull(bundle).getString("queryString"));
        //return new MatchLoader(this, Objects.requireNonNull(bundle).getString("queryString"));
        return new FollowLoader(this, Objects.requireNonNull(bundle).getString("queryString"));
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

        // Method call to test player GET.
        //TestPlayerGETBackEnd(s);

        // Method call to test sport GET.
        //TestSportGETBackend(s);

        // Method call to test match GET.
        //TestMatchGETBackend(s);

        // Method call to test match GET.
        TestFollowGETBackend(s);
    }

    private void TestFollowGETBackend(String s) {

        // If string s is empty, then connection failed.
        if (s.contains("Connection failed!")){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.connection_failed);
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.length() == 0){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_results_found);
            Toast.makeText(this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // obtain the JSON array of results items
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray;

            // Condition to handle getting the player list
            if(queryString.length() == 0){
                itemsArray = jsonObject.getJSONArray("items");

                Log.e(LOG_TAG, "Length of itemsArray: " + itemsArray.length());

                //Iterate through the results
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject follow = itemsArray.getJSONObject(i); //Get the current item
                    String id = "id";
                    String sportID = "sportID";
                    String playerID = "playerID";
                    String rank = "rank";

                    try {
                        id = follow.getString("id");
                        sportID = follow.getString("sportID");
                        playerID = follow.getString("playerID");
                        rank = follow.getString("rank");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //If information field requested exists, update the TextViews and return
                    if (id != null || sportID != null || playerID != null || rank != null)  {
                        textViewSearchResults.append("\n\nid: " + id + "\n" + "sportID: " + sportID + "\n"
                                + "playerID: " + playerID + "\n" + "rank: " + rank + "\n");
                    }
                    else{
                        textViewSearchResults.append("\n\nFailure to retrieve any information for this particular follow!\n");
                    }
                }
                return;
            }
            // Condition to get a specific player in list
            else{
                String id = "id";
                String sportID = "sportID";
                String playerID = "playerID";
                String rank = "rank";

                try {
                    id = jsonObject.getString("id");
                    sportID = jsonObject.getString("sportID");
                    playerID = jsonObject.getString("playerID");
                    rank = jsonObject.getString("rank");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //If information field requested exists, update the TextViews and return
                if (id != null || sportID != null || playerID != null || rank != null)  {
                    textViewSearchResults.append("\n\nid: " + id + "\n" + "sportID: " + sportID + "\n"
                            + "playerID: " + playerID + "\n" + "rank: " + rank + "\n");
                    return;
                }
            }

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.display_failure);
            Toast.makeText(this, R.string.display_failure, Toast.LENGTH_SHORT).show();

        } catch (Exception ex){

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.json_failure);
            Toast.makeText(this, R.string.json_failure, Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        } finally{
            Log.e(LOG_TAG,"Finished query process!");
        }
    }

    /**
     * Method to test retrieval of Match Table values.
     *
     * @param s string storing JSON result object.
     */
    private void TestMatchGETBackend(String s) {

        // If string s is empty, then connection failed.
        if (s.contains("Connection failed!")){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.connection_failed);
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.length() == 0){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_results_found);
            Toast.makeText(this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // obtain the JSON array of results items
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray;

            // Condition to handle getting the player list
            if(queryString.length() == 0){
                itemsArray = jsonObject.getJSONArray("items");

                Log.e(LOG_TAG, "Length of itemsArray: " + itemsArray.length());

                //Iterate through the results
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject match = itemsArray.getJSONObject(i); //Get the current item
                    String id = "id";
                    String sportID = "sportID";
                    String playerOneID = "playerOneID";
                    String playerTwoID = "playerTwoID";
                    String playerOneScore = "playerOneScore";
                    String playerTwoScore = "playerTwoScore";
                    String winner = "winner";
                    String time = "time";
                    String verified = "verified";

                    try {
                        id = match.getString("id");
                        sportID = match.getString("sportID");
                        playerOneID = match.getString("playerOneID");
                        playerTwoID = match.getString("playerTwoID");
                        playerOneScore = match.getString("playerOneScore");
                        playerTwoScore = match.getString("playerTwoScore");
                        winner = match.getString("winner");
                        time = match.getString("time");
                        verified = match.getString("verified");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //If information field requested exists, update the TextViews and return
                    if (id != null || sportID != null || playerOneID != null || playerTwoID != null
                            || playerOneScore != null || playerTwoScore != null || winner != null
                            || time != null || verified != null)  {
                        textViewSearchResults.append("\n\nid: " + id + "\n" + "sportID: " + sportID
                                + "\n" + "playerOneID: " + playerOneID + "\n"+ "playerTwoID: " + playerTwoID + "\n"
                                + "playerOneScore: " + playerOneScore + "\n" + "playerTwoScore: " + playerTwoScore + "\n"
                                + "winner: " + winner + "\n" + "time: " + time + "\n"
                                + "verified: " + verified + "\n");
                    }
                    else{
                        textViewSearchResults.append("\n\nFailure to retrieve any information for this particular match!\n");
                    }
                }
                return;
            }
            // Condition to get a specific player in list
            else{
                String id = "no id found";
                String sportID = "no sport ID found";
                String playerOneID = "no player one ID found";
                String playerTwoID = "no player two ID found";
                String playerOneScore = "no player one score found";
                String playerTwoScore = "no player two score found";
                String winner = "no winner found";
                String time = "no time found";
                String verified = "no verified found";

                try {
                    id = jsonObject.getString("id");
                    sportID = jsonObject.getString("sportID");
                    playerOneID = jsonObject.getString("playerOneID");
                    playerTwoID = jsonObject.getString("playerTwoID");
                    playerOneScore = jsonObject.getString("playerOneScore");
                    playerTwoScore = jsonObject.getString("playerTwoScore");
                    winner = jsonObject.getString("winner");
                    time = jsonObject.getString("time");
                    verified = jsonObject.getString("verified");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //If information field requested exists, update the TextViews and return
                if (id != null || sportID != null || playerOneID != null || playerTwoID != null
                        || playerOneScore != null || playerTwoScore != null || winner != null
                        || time != null || verified != null)  {
                    textViewSearchResults.append("\n\nid: " + id + "\n" + "sportID: " + sportID
                            + "\n" + "playerOneID: " + playerOneID + "\n"+ "playerTwoID: " + playerTwoID + "\n"
                            + "playerOneScore: " + playerOneScore + "\n" + "playerTwoScore: " + playerTwoScore + "\n"
                            + "winner: " + winner + "\n" + "time: " + time + "\n"
                            + "verified: " + verified + "\n");
                    return;
                }
            }

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.display_failure);
            Toast.makeText(this, R.string.display_failure, Toast.LENGTH_SHORT).show();

        } catch (Exception ex){

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.json_failure);
            Toast.makeText(this, R.string.json_failure, Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        } finally{
            Log.e(LOG_TAG,"Finished query process!");
        }
    }

    /**
     * Method to test retrieval of Sport Table values.
     *
     * @param s string storing JSON result object.
     */
    private void TestSportGETBackend(String s) {

        // If string s is empty, then connection failed.
        if (s.contains("Connection failed!")){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.connection_failed);
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.length() == 0){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_results_found);
            Toast.makeText(this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // obtain the JSON array of results items
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray;

            // Condition to handle getting the player list
            if(queryString.length() == 0){
                itemsArray = jsonObject.getJSONArray("items");

                Log.e(LOG_TAG, "Length of itemsArray: " + itemsArray.length());

                //Iterate through the results
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject sport = itemsArray.getJSONObject(i); //Get the current item
                    String id = "id";
                    String name = "name";
                    String type = "type";

                    try {
                        id = sport.getString("id");
                        name = sport.getString("name");
                        type = sport.getString("type");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //If information field requested exists, update the TextViews and return
                    if (id != null || name != null || type != null)  {
                        textViewSearchResults.append("\n\nid: " + id + "\n" + "name: " + name + "\n" + "type: " + type + "\n");
                    }
                    else{
                        textViewSearchResults.append("\n\nFailure to retrieve any information for this particular sport!\n");
                    }
                }
                return;
            }
            // Condition to get a specific player in list
            else{
                String id = "no id found";
                String name = "no name found";
                String type = "no type found";

                try {
                    id = jsonObject.getString("id");
                    name = jsonObject.getString("name");
                    type = jsonObject.getString("type");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //If information field requested exists, update the TextViews and return
                if (id != null || name != null || type != null) {
                    textViewSearchResults.append("\n\nid: " + id + "\n" + "name: " + name + "\n" + "type: " + type + "\n");
                    return;
                }
            }

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.display_failure);
            Toast.makeText(this, R.string.display_failure, Toast.LENGTH_SHORT).show();

        } catch (Exception ex){

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.json_failure);
            Toast.makeText(this, R.string.json_failure, Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        } finally{
            Log.e(LOG_TAG,"Finished query process!");
        }
    }

    /**
     * Method to test retrieval of Player Table values.
     *
     * @param s string storing JSON result object.
     */
    private void TestPlayerGETBackEnd(String s) {

        // If string s is empty, then connection failed.
        if (s.contains("Connection failed!")){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.connection_failed);
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.length() == 0){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_results_found);
            Toast.makeText(this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // obtain the JSON array of results items
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray;

            // Condition to handle getting the player list
            if(queryString.length() == 0){
                itemsArray = jsonObject.getJSONArray("items");

                Log.e(LOG_TAG, "Length of itemsArray: " + itemsArray.length());

                //Iterate through the results
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject player = itemsArray.getJSONObject(i); //Get the current item
                    String id = "id";
                    String emailAddress = "emailAddress";
                    String accountCreationDate = "default";

                    try {
                        id = player.getString("id");
                        emailAddress = player.getString("emailAddress");
                        accountCreationDate = player.getString("accountCreationDate");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //If information field requested exists, update the TextViews and return
                    if (id != null || emailAddress != null || accountCreationDate != null)  {
                        textViewSearchResults.append("\n\nid: " + id + "\n" + "emailAddress: " + emailAddress + "\n" + "accountCreationDate: " + accountCreationDate + "\n");
                    }
                    else{
                        textViewSearchResults.append("\n\nFailure to retrieve any information for this particular player!\n");
                    }
                }
                return;
            }
            // Condition to get a specific player in list
            else{
                String id = "no id found";
                String emailAddress = "no email address found";
                String accountCreationDate = "no account creation date found";

                try {
                    id = jsonObject.getString("id");
                    emailAddress = jsonObject.getString("emailAddress");
                    accountCreationDate = jsonObject.getString("accountCreationDate");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //If information field requested exists, update the TextViews and return
                if (id != null || emailAddress != null || accountCreationDate != null) {
                    textViewSearchResults.append("\n\nid: " + id + "\n" + "emailAddress: " + emailAddress + "\n" + "accountCreationDate: " + accountCreationDate + "\n");
                    return;
                }
            }

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.display_failure);
            Toast.makeText(this, R.string.display_failure, Toast.LENGTH_SHORT).show();

        } catch (Exception ex){

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.json_failure);
            Toast.makeText(this, R.string.json_failure, Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        } finally{
            Log.e(LOG_TAG,"Finished query process!");
        }
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
