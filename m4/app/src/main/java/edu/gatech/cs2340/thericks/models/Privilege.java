package edu.gatech.cs2340.thericks.models;

/**
 * Created by mkcac on 9/29/2017.
 */

public enum Privilege {
    NORMAL("Normal"),
    ADMIN("Admin");

    private String label;

    Privilege(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
