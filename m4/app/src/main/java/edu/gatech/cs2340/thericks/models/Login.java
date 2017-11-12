package edu.gatech.cs2340.thericks.models;

import java.io.Serializable;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a single user's login information.  It implements serializable so it can be
 * passed as a parcel from the login screen.
 */
public class Login implements Serializable {
    private static final String TAG = Login.class.getSimpleName();

    /** Username for login **/
    private final String username;

    /** The hashed password for the login **/
    private final String securePassword;

    /** Random salt for hashing **/
    private final String salt;


    Login(String username, String password, String salt) {
        this.username = username;
        this.salt = salt;
        securePassword = password;
    }


    public String getUsername() {
        return username;
    }

    public String getSecurePassword() {
        return securePassword;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return String.format("Username: %s\nEncrypted Password: %s\n", username, getSecurePassword());
    }
}
