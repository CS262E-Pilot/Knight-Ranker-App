package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PastMatchAdapter extends ArrayAdapter<PastMatch> {
    private final Context mContext;

    public PastMatchAdapter(Context context, ArrayList<PastMatch> data) {
        super(context, R.layout.past_match_layout, data);
        this.mContext = context;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        PastMatch pastMatchItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.past_match_layout, parent, false);
        }

        if (pastMatchItem != null) {
            // Set player one email
            TextView playerOneName = (convertView.findViewById(R.id.playerOneName));
            playerOneName.setText(String.valueOf(pastMatchItem.getPlayerName()));
            // Set player two email
            TextView playerText = (convertView.findViewById(R.id.playerTwoName));
            playerText.setText(pastMatchItem.getOpponentName());
            // Set player player one score
            TextView playerOneScore = (convertView.findViewById(R.id.playerOneScore));
            playerOneName.setText(String.valueOf(pastMatchItem.getPlayerOneScore()));
            // Set player player two score
            TextView playerTwoScore = (convertView.findViewById(R.id.playerTwoScore));
            playerOneName.setText(String.valueOf(pastMatchItem.getPlayerTwoScore()));
            // Set timestamp
            TextView timestamp = (convertView.findViewById(R.id.timestamp));
            playerOneName.setText(String.valueOf(pastMatchItem.getTimestamp()));
        }
        return convertView;
    }
}
