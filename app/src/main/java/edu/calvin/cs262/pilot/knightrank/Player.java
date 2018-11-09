package edu.calvin.cs262.pilot.knightrank;

/**
 * Sport class that models a sport
 */
public class Player {
    private int id;
    private String name;
    private int rank;

    public Player(int id, String name, int rank) {
        this.id = id;
        this.name = name;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }
}
