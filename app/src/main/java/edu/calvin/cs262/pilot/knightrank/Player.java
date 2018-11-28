package edu.calvin.cs262.pilot.knightrank;

/**
 * Class Player defines and models a player.
 */
public class Player {
    private int id;
    private String name;
    private String emailAddress;

    public Player() {}

    public Player(int id, String name, String emailAddress) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
