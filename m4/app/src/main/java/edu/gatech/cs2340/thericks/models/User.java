package edu.gatech.cs2340.thericks.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a user of the rat-tracker application.
 */

public class User implements Parcelable {
    private static final String TAG = User.class.getSimpleName();

    /** User's secure login data **/
    private Login loginInfo;

    /** User's privilege status - either normal or admin**/
    private Privilege privilege;

    /** User's login status **/
    private boolean loggedIn;

    // Constructor for a new User Object
    public User(String username, String password, String salt, Privilege privilege) {
        // Generate secure login information
        loginInfo = new Login(username, password, salt);
        loggedIn = false;
        this.privilege = privilege;
        Log.d(TAG, username + password + salt + privilege.toString());
    }

    public User(String username, String password, Privilege privilege) {
        this(username, password, Security.generateSalt(), privilege);
    }

    public String getUsername() {
        if (loginInfo != null) {
            return loginInfo.getUsername();
        } else {
            return "";
        }
    }

    public void login() {
        loggedIn = true;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Login getLogin() {
        return loginInfo;
    }



    /* *****************************************************
     * METHODS FOR IMPLEMENTING PARCELABLE
     * *****************************************************/

    private User(Parcel in) {
        loginInfo = (Login) in.readSerializable();
        loggedIn = in.readByte() != 0;
        privilege = (Privilege) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(loginInfo);
        dest.writeByte((byte) (loggedIn ? 1: 0));
        dest.writeSerializable(privilege);

    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /* *****************************************************/
}
