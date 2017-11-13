package edu.gatech.cs2340.thericks.database;

import android.database.sqlite.SQLiteDatabase;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserDataSource;

/**
 * Class representing a database of users.  This class is used as an interface between the user
 * database and the activities.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

public class UserDatabase implements UserDataSource {
    // --Commented out by Inspection (11/13/2017 1:30 AM):private static final String TAG = UserDatabase.class.getSimpleName();
    private SQLiteDatabase db;

    /**
     * Constructor that initializes SQLite Database to a UserDatabaseHandler
     */
    public UserDatabase() {
        open();
    }

    private void open() {
        db = UserDatabaseHandler.provideWritableDatabase();
    }

    @Override
    public void createUser(String username, String password, Privilege privilege) {
        UserDAO.createUser(db, username, password, privilege);
    }

// --Commented out by Inspection START (11/13/2017 1:33 AM):
//    @Override
//    public void deleteUser(String username) {
//        UserDAO.deleteUser(db, username);
//    }
// --Commented out by Inspection STOP (11/13/2017 1:33 AM)

    @Override
    public User getUserByUsername(String username) {
        return UserDAO.getUserByUsername(db, username);
    }

// --Commented out by Inspection START (11/13/2017 1:33 AM):
//    @Override
//    public List<User> getAllUsers() {
//        return UserDAO.getAllUsers(db);
//    }
// --Commented out by Inspection STOP (11/13/2017 1:33 AM)
}
