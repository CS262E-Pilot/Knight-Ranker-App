package edu.calvin.cs262.pilot.knightrank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Class PlayerRankAdapter defines and configures a ArrayAdapter for the purpose of displaying
 * player rankings.
 */
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
            // Set player rank
            TextView playerRank = (convertView.findViewById(R.id.player_rank));
            playerRank.setText(String.valueOf(position + 1).concat("."));
            // Set player email
            TextView playerText = (convertView.findViewById(R.id.player_rank_name));
            playerText.setText(playerItem.getName().concat(", "));
            // Set player elo rank
            TextView playerEloRank = (convertView.findViewById(R.id.player_rank_elo));
            playerEloRank.setText("Elo: ".concat(String.valueOf(playerItem.getEloRank())));
        }
        return convertView;
    }
}
