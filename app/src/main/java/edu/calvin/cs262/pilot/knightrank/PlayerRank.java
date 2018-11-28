package edu.calvin.cs262.pilot.knightrank;

/**
 * Class PlayerRank defines and models a player's rank based on the "eloRank" algorithm.
 */
public class PlayerRank {
    private int eloRank;
    private String name;

    public PlayerRank() {}

    public PlayerRank(int eloRank, String emailAddress) {
        this.eloRank = eloRank;
        this.name = emailAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getEloRank() {
        return eloRank;
    }

    public void setEloRank(int eloRank) {
        this.eloRank = eloRank;
    }
}
