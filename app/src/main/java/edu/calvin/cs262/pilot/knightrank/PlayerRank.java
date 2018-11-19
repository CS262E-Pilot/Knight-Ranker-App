package edu.calvin.cs262.pilot.knightrank;

public class PlayerRank {
    private int rank;
    private String emailAddress;

    public PlayerRank() {}

    public PlayerRank(int rank, String emailAddress) {
        this.rank = rank;
        this.emailAddress = emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
