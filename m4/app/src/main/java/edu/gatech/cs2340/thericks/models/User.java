package edu.gatech.cs2340.thericks.models;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a user of the rat-tracker application.
 */

public class User {

    /** User's secure login data **/
    private Login loginInfo;

    /** User's login status **/
    private boolean loggedIn;


    /**
     * Constructor for new user
     * @param username user's username
     * @param password user's unencrypted password
     */
    public User(String username, String password) {
        // Generate secure login information
        loginInfo = new Login(username, password);
        loggedIn = false;
    }




    /**
     *
     * @return user's login information
     */
    public Login getLogin() {
        return loginInfo;
    }
}
