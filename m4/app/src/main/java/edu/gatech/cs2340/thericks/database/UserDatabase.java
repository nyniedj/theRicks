package edu.gatech.cs2340.thericks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import edu.gatech.cs2340.thericks.models.Privilege;
import edu.gatech.cs2340.thericks.models.User;
import edu.gatech.cs2340.thericks.models.UserDataSource;

/**
 * Class representing a database of users.  This class is used as an interface between the user database and the activities.
 *
 * Created by Ben Lashley on 10/31/2017.
 */

public class UserDatabase implements UserDataSource {
    private static final String TAG = UserDatabase.class.getSimpleName();
    private SQLiteDatabase db;

    public UserDatabase(Context context) {
        open();
    }

    private void open() {
        db = UserDatabaseHandler.provideWritableDatabase();
    }

    @Override
    public void createUser(String username, String password, Privilege privilege) {
        UserDAO.createUser(db, username, password, privilege);
    }

    @Override
    public void deleteUser(String username) {
        UserDAO.deleteUser(db, username);
    }

    @Override
    public User getUserByUsername(String username) {
        return UserDAO.getUserByUsername(db, username);
    }

    @Override
    public List<User> getAllUsers() {
        return UserDAO.getAllUsers(db);
    }
}
