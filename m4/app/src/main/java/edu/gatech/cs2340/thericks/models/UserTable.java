package edu.gatech.cs2340.thericks.models;

import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Created by Ben Lashley on 9/19/2017.
 *
 * Singleton class that holds list of registered users.
 */

public class UserTable {

    /** Singleton instance **/
    private static final UserTable instance = new UserTable();
    public static UserTable getInstance() {
        return instance;
    }

    /** List of registered users **/
    private List<User> users;

    /**
     * Constructor for empty user table.
     */
    private UserTable() {
        users = new ArrayList<>();
    }

    public void loadDummyData() {
        addUserFromData("username", "Passw0rd");
    }

    /**
     * Creates new user with desired username and password. Then adds new user to user table.
     *
     * @param  username Desired username
     * @param  password Desired (unencrypted) password
     * @return true if user was created and added successfully, false if not
     */
    public boolean addUserFromData(String username, String password) {
        // Check username availability
        for (User u : users) {
            if (u.getLogin().getUsername().equals(username)) {
                return false;
            }
        }
        // Create new user
        User newUser = new User(username, password);
        return addUser(newUser);
    }

    /**
     * Adds existing user to user table if login is valid
     * @param u User to be added
     * @return true if user was added successfully, false if not
     */
    public boolean addUser(User u) {
        if (u.getLogin().isValid()) {
            users.add(u);
            Log.d("Login", "Added new user: " + u.getLogin().getUsername());
            return true;
        } else {
            return false;
        }
    }


    /**
     * Removes user with provided username and password from table.
     *
     * @param username The username of the user to delete
     * @param password The password of the user to delete
     * @return true if user was deleted successfully, false if not
     */
    public boolean deleteUser(String username, String password) {
        User u = getUserByUsername(username);
        if (u == null) {
            Log.d("Login", "Cannot delete: no such user");
            return false;
        } else if (u.getLogin() == null) {
            Log.d("Login", "Removed user with missing login.");
            users.remove(u);
            return true;
        }
        if (Security.checkPassword(password, u.getLogin())) {
            Log.d("Login", "Removed user: " + u.getLogin().getUsername());
            users.remove(u);
            return true;
        } else {
            Log.d("Login", "Attempt to delete user: " + u.getLogin().getPasswordString() + " failed. Invalid password.");
            return false;
        }
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

    /**
     *
     * @return Number of registered users
     */
    public int size() {
        return users.size();
    }
}
