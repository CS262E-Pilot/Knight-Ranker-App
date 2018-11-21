package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerRankAdapter extends ArrayAdapter<PlayerRank> {
    private final Context mContext;

    public PlayerRankAdapter(Context context, ArrayList<PlayerRank> data) {
        super(context, R.layout.player_rank_item, data);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        PlayerRank playerItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.player_rank_item, parent, false);
        }

        if (playerItem != null) {
            // Set player elo rank
            TextView playerEloRank = (convertView.findViewById(R.id.player_rank_elo));
            playerEloRank.setText(String.valueOf(playerItem.getEloRank()));
            // Set player email
            TextView playerText = (convertView.findViewById(R.id.player_rank_name));
            playerText.setText(playerItem.getName());
        }
        return convertView;
    }
}
