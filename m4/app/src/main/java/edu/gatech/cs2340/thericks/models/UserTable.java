package edu.gatech.cs2340.thericks.models;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by Ben Lashley on 9/19/2017.
 */

public class UserTable {

    /** List of registered users **/
    private List<User> users = new ArrayList<>();

    /** See validatePassword() for complete password criteria **/
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 32;

    private static final int MIN_USERNAME_LENGTH = 6;
    private static final int MAX_USERNAME_LENGTH = 32;

    /**
     * Constructor for a new user table with pre-registered users.
     *
     * @param oldUsers List of registered users to include.
     */
    public UserTable(List<User> oldUsers) {
        this.users.addAll(oldUsers);
    }

    /**
     * Constructor for empty user table.
     */
    public UserTable() {
        this(Collections.<User>emptyList());
    }

    /**
     * Creates new user with desired username and password. Then adds new user to user table.
     *
     * @param  username Desired name for user to create
     * @param  password Desired (unencrypted) password for
     * @return true if user was created and added successfully, false if not
     */
    public boolean addUserFromData(String username, String password) {
        // Validate username and password
        if (!validateUsername(username) || !validatePassword(password)) {
            return false;
        }
        // Create user
        User newUser = new User(username, password);
        if (newUser.getLogin().getSecurePassword() != null) {
            // User was successfully created, so add to user table
            users.add(newUser);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a new user's desired username is valid and available to use.
     *
     * @param username the desired username
     * @return true if valid and available, false if not
     */
    private boolean validateUsername(String username) {
        // Check if username is valid
        if (username == null) {
            return false;
        } else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            return false;
        }
        // Check if username is available
        for (User u : users) {
            if (u.getLogin().getUsername().equals(username)) {
                return false;
            }
        }
        // Valid, available username
        return true;
    }

    /**
     * Checks if password meets criteria to be considered valid.
     * Criteria (must meet all):
     *  (?=.*[0-9]) at least one digit
     *  (?=.*[a-z]) at least one lowercase letter
     *  (?=.*[A-Z]) at least one uppercase letter
     *  (?=\\S+$)   no whitespace
     *  .{MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH}     length between min and max (inclusive)
     *
     * @param pw the password to validate
     * @return true if password is valid, false if not
     */
    private boolean validatePassword(String pw) {
        final Pattern pat = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{"+ MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "}$");
        return pw != null && pat.matcher(pw).matches();
    }

    /**
     * Fetches user with specified username from table.
     *
     * @param username the username to search for
     * @return the user with that username, if they exist; null if no user has that username
     */
    public User getUserByUsername(String username) {
        for (User u : users) {
            if (u.getLogin().getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
