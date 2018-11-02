package edu.calvin.cs262.pilot.knightrank;

/**
 * Sport class that models a sport
 */
public class Sport {
    private int id;
    private String name;

    public Sport(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
