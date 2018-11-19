package edu.calvin.cs262.pilot.knightrank;

public class PlayerRank {
    private int eloRank;
    private String emailAddress;

    public PlayerRank() {}

    public PlayerRank(int eloRank, String emailAddress) {
        this.eloRank = eloRank;
        this.emailAddress = emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getEloRank() {
        return eloRank;
    }

    public void setEloRank(int eloRank) {
        this.eloRank = eloRank;
    }
}
