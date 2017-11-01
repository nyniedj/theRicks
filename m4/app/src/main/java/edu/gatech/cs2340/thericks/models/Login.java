package edu.gatech.cs2340.thericks.models;

import android.util.Log;

import java.io.Serializable;

import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Created by Ben Lashley on 9/18/2017.
 *
 * Class represents a single user's login information.  It implements serializable so it can be
 * passed as a parcel from the login screen.
 */
public class Login implements Serializable {
    private static final String TAG = Login.class.getSimpleName();

    /** Username for login **/
    private String username;

    /** The hashed password for the login **/
    private String securePassword;

    /** Random salt for hashing **/
    private String salt;


    /**
     * Constructor for new login information with pre-encrypted password
     *
     * @param username user's username
     * @param password user's un-hashed password
     * @param salt the salt used for password encryption
     */
    Login(String username, String password, String salt) {
        this.username = username;
        this.salt = salt;
        securePassword = password;
    }


    /**
     *
     * @return Username for login
     */
    public String getUsername() {
        return username;
    }


    /**
     *
     * @return secure password
     */
    public String getSecurePassword() {
        return securePassword;
    }


    /**
     *
     * @return salt used to encrypt login password
     */
    public String getSalt() {
        return salt;
    }


    /**
     *
     * @return Login information as a string
     */
    public String toString() {
        return String.format("Username: %s\nEncrypted Password: %s\n", username, getSecurePassword());
    }
}
