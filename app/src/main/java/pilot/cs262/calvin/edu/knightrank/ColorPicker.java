package pilot.cs262.calvin.edu.knightrank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * This class implements a color picker.
 * Uses pref_color_pick.xml for the layout file.
 *
 * Note: Will not function as part of SettingsActivity.java due to unknown reasons.
 */

public class ColorPicker extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String LOG_TAG =
            ColorPicker.class.getSimpleName();

    //Reference the seek bars
    private SeekBar SeekA;
    private SeekBar SeekR;
    private SeekBar SeekG;
    private SeekBar SeekB;

    // Store the current color.
    private int[] myColor = {Color.BLACK};

    //Reference the TextView
    private TextView ShowColor;

    // For use with shared preferences.
    // Note: Don't make private so we can access the key's name in other classes.
    static final String COLOR_ARGB = "ARGB color";

    private DrawerLayout mDrawerLayout;

    // Share preferences file (custom)
    private SharedPreferences mPreferences;
    // Shared preferences file (default)
    private SharedPreferences mPreferencesDefault;

    // Name of the custom shared preferences file.
    private static final String sharedPrefFile = "pilot.cs262.calvin.edu.knightrank";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        //Get a reference to the seekbars
        SeekA = findViewById(R.id.seekA);
        SeekR = findViewById(R.id.seekR);
        SeekG = findViewById(R.id.seekG);
        SeekB = findViewById(R.id.seekB);

        //Reference the TextView
        ShowColor = findViewById(R.id.textView);

        //This activity implements SeekBar OnSeekBarChangeListener
        SeekA.setOnSeekBarChangeListener(this);
        SeekR.setOnSeekBarChangeListener(this);
        SeekG.setOnSeekBarChangeListener(this);
        SeekB.setOnSeekBarChangeListener(this);

        // my_child_toolbar is defined in the layout file
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Custom icon for the Up button
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        // Set shared preferences component.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(this);

        // Placeholder code as example of how to get values from the default SharedPrefs file.
        String syncFreq = mPreferencesDefault.getString(SettingsActivity.KEY_SYNC_FREQUENCY, "-1");

        // Placeholder code as example of how to restore values to UI components from shared preferences.
        //username_main.setText(mPreferences.getString(USER_NAME, ""));
        //password_main.setText(mPreferences.getString(USER_PASSWORD, ""));
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
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to perform action during onPause() life-cycle state.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to track changes in the color picker.
     *
     * @param seekBar  object
     * @param progress object
     * @param fromUser object
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //get current ARGB values
        // Store ARGB values.
        int avalue = SeekA.getProgress();
        int rvalue = SeekR.getProgress();
        int gvalue = SeekG.getProgress();
        int bvalue = SeekB.getProgress();

        //Reference the value changing
        int id = seekBar.getId();

        //Get the changed value
        if (id == pilot.cs262.calvin.edu.knightrank.R.id.seekA)
            avalue = progress;
        else if (id == pilot.cs262.calvin.edu.knightrank.R.id.seekR)
            rvalue = progress;
        else if (id == pilot.cs262.calvin.edu.knightrank.R.id.seekG)
            gvalue = progress;
        else if (id == pilot.cs262.calvin.edu.knightrank.R.id.seekB)
            bvalue = progress;

        //Build and show the new color
        ShowColor.setBackgroundColor(Color.argb(avalue, rvalue, gvalue, bvalue));

        // Store color object in int array.
        myColor[0] = Color.argb(avalue, rvalue, gvalue, bvalue);

        //show the color value
        ShowColor.setText("0x" + String.format("%02x", avalue) + String.format("%02x", rvalue)
                + String.format("%02x", gvalue) + String.format("%02x", bvalue));

        //some math so text shows (needs improvement for greys)
        ShowColor.setTextColor(Color.argb(0xff, 255 - rvalue, 255 - gvalue, 255 - bvalue));
    }

    /**
     * Method required due to implements.
     *
     * @param seekBar object
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Only required due to implements
    }

    /**
     * Method required due to implements.
     *
     * @param seekBar object
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //Only required due to implements
    }

    /**
     * Button click method to change app background color.
     *
     * Note: This is a test.  Will find a better way to do this later on.
     *
     * @param view object
     */
    public void changeAppBackgroundColor(View view) {

        SharedPreferences.Editor preferencesEditor3 = mPreferences.edit();

        preferencesEditor3.putInt(COLOR_ARGB, myColor[0]);

        preferencesEditor3.apply();

        int value = mPreferences.getInt(COLOR_ARGB, Color.WHITE);

        // Debug purposes
        Log.e(LOG_TAG, "value of color key is: " + value);
        Log.e(LOG_TAG, "value of color array is: " + myColor[0]);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
}
