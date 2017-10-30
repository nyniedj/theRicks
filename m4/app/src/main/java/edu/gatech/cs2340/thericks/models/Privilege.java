package edu.gatech.cs2340.thericks.models;

/**
 * Created by Cameron on 9/29/2017.
 * Enumerated class for different levels of user privilege
 */
public enum Privilege {
    NORMAL("Normal"),
    ADMIN("Admin");

    private String label;

    /**
     *
     * @param label
     */
    Privilege(String label) {
        this.label = label;
    }

    /**
     *
     * @return label
     */
    public String toString() {
        return label;
    }
}
