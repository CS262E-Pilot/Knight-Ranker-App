package edu.calvin.cs262.pilot.knightrank;

public class Match {
    private int id;
    private String timestamp;
    private String verified;
    private int winner;
    private int playerOneScore;
    private int playerTwoScore;
    private int playerOneID;
    private int playerTwoID;
    private int sportID;

    public Match(int id, String timestamp, String verified, int winner, int playerOneScore, int playerTwoScore, int playerOneID, int playerTwoID, int sportID) {
        this.id = id;
        this.timestamp = timestamp;
        this.verified = verified;
        this.winner = winner;
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this.playerOneID = playerOneID;
        this.playerTwoID = playerTwoID;
        this.sportID = sportID;
    }
}
