package edu.calvin.cs262.pilot.knightrank;

/**
 * Class ConfirmItemDTO defines getter and setter methods for the ConfirmCheckboxActivity.java
 * class that relate to the list of unresolved matches populated from the Knight-Ranker
 * PostgreSQL database.
 */
public class ConfirmItemDTO {

    private boolean checked = false;

    private String itemText = "";

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
}
