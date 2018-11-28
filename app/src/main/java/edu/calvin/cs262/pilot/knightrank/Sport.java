package edu.calvin.cs262.pilot.knightrank;

/**
 * Class Sport defines and models a sport activity.
 */
public class Sport {
    private int id;
    private String name;
    private String type;

    public Sport(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
