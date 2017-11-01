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


    /**
     * Constructor for new user
     * @param username user's username
     * @param password user's encrypted password
     * @param privilege user's privilege status
     */
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

    /**
     *
     * @return input username or empty string
     */
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

    /**
     *
     * @return true/false if user is logged in
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     *
     * @return user's login information
     */
    public Login getLogin() {
        return loginInfo;
    }



    /* *****************************************************
     * METHODS FOR IMPLEMENTING PARCELABLE
     * *****************************************************/

    /**
     *
     * @param in parcel to create user from
     */
    private User(Parcel in) {
        loginInfo = (Login) in.readSerializable();
        loggedIn = in.readByte() != 0;
        privilege = (Privilege) in.readSerializable();
    }

    /**
     *
     * @return zero
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest Parcel to write user info to
     * @param flags Flags
     */
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
