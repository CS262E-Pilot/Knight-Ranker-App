package edu.calvin.cs262.pilot.knightrank;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import edu.calvin.cs262.pilot.knightrank.R;

public class SportSelection extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mSportListView;
    private FloatingActionButton mFab;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSportListView = findViewById(R.id.sport_list);
        mSportListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, new String[] {
                "Math",
                "Sports",
                "SSBM"
        }));
        mSportListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSportListView.setItemChecked(2, true);

        mSportListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View arg1, int arg2, long arg3)
    {
        SparseBooleanArray sp = mSportListView.getCheckedItemPositions();
        mFab.setVisibility(View.VISIBLE);
    }

}
