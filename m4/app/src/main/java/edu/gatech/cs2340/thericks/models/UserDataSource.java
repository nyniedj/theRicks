package edu.gatech.cs2340.thericks.models;

import java.util.List;

/**
 * Interface for a source of user data with basic operations for managing a user table.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

public interface UserDataSource {
    void createUser(String username, String password, Privilege privilege);
    void deleteUser(String username);
    User getUserByUsername(String username);
    List<User> getAllUsers();
}
