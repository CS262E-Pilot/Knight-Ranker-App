package edu.calvin.cs262.pilot.knightrank;

public class PastMatch {
    private int id;
    private String timestamp;
    private int playerScore;
    private int opponentScore;
    private String playerName;
    private String opponentName;

    public PastMatch(int id, String timestamp, int playerScore, int opponentScore, String playerName, String opponentName) {
        this.id = id;
        this.timestamp = timestamp;
        this.playerScore = playerScore;
        this.opponentScore = opponentScore;
        this.playerName = playerName;
        this.opponentName = opponentName;
    }

    public int getPlayerOneScore() {
        return playerScore;
    }

    public int getPlayerTwoScore() {
        return opponentScore;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }
}
