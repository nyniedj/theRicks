package edu.gatech.cs2340.thericks.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a user of the rat-tracker application.
 */

public class User implements Parcelable {

    /** User's secure login data **/
    private Login loginInfo;

    private Privilege privilege;

    /** User's login status **/
    private boolean loggedIn;


    /**
     * Constructor for new user
     * @param username user's username
     * @param password user's unencrypted password
     */
    public User(String username, String password, Privilege privilege) {
        // Generate secure login information
        loginInfo = new Login(username, password);
        loggedIn = false;
        this.privilege = privilege;
    }

    public Privilege getPrivilege() {
        return privilege;
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

    /**
     *
     * @return user's login information
     */
    public Login getLogin() {
        return loginInfo;
    }



    /*******************************************************
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

    /** ****************************************************/
}
