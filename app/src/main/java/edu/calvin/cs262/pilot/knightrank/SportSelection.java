package edu.calvin.cs262.pilot.knightrank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import edu.calvin.cs262.pilot.knightrank.R;

public class SportSelection extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mSportListView;
    private FloatingActionButton mFab;

    private String[] testData = new String[] {
            "Math",
            "Tennis",
            "Super Smash Bros. Melee"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_selection);

        mFab = findViewById(R.id.fab);
        // Make the fab invisible until we have some sports checked
        mFab.setVisibility(View.INVISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(view);
            }
        });

        mSportListView = findViewById(R.id.sport_list);
        // Some test data
        mSportListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, testData));
        mSportListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSportListView.setOnItemClickListener(this);
    }

    public void startActivity(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE).edit();
        // Add all our checked sports into the shared preferences so we can reference them later
        SparseBooleanArray checked = mSportListView.getCheckedItemPositions();
        Set<String> selectedSports = new HashSet<>();
        for (int i = 0; i < mSportListView.getAdapter().getCount(); i++) {
            // If we have a check, show the button and stop looping
            if (checked.get(i)) {
                selectedSports.add(testData[i]);
            }
        }
        editor.putStringSet(getString(R.string.selected_sports), selectedSports).apply();

        // Start our new intent
        Intent intent = new Intent(this, ActivityRankings.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View arg1, int arg2, long arg3)
    {
        // Loop over the checked array and show the continue button if we have at least one check
        SparseBooleanArray checked = mSportListView.getCheckedItemPositions();
        for (int i = 0; i < mSportListView.getAdapter().getCount(); i++) {
            // If we have a check, show the button and stop looping
            if (checked.get(i)) {
                mFab.setVisibility(View.VISIBLE);
                return;
            }
        }
        // We haven't returned so that means we have no checks, so hide the continue button
        mFab.setVisibility(View.INVISIBLE);
    }

}
