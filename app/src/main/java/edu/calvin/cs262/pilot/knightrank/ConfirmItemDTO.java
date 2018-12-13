package edu.calvin.cs262.pilot.knightrank;

/**
 * Class ConfirmItemDTO defines getter and setter methods for the ConfirmCheckboxActivity.java
 * class that relate to the list of unresolved matches populated from the Knight-Ranker
 * PostgreSQL database.
 */
public class ConfirmItemDTO {
    private int id;
    private String sport;
    private String playerName;
    private String opponentName;
    private int playerScore;
    private int opponentScore;
    private boolean checked = false;

    ConfirmItemDTO(int id, String sport, String playerName, String opponentName, int playerScore, int opponentScore) {
        this.id = id;
        this.sport = sport;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerScore = playerScore;
        this.opponentScore = opponentScore;
    }

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getItemText() {
        return sport + "\n" + playerName + ":" + playerScore + "\n" + opponentName + ":" + opponentScore;
    }

}
