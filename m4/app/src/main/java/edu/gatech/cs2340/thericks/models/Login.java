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

    /** Username for login **/
    private String username;

    /** The hashed password for the login **/
    private byte[] securePassword;

    /** Random salt for hashing **/
    private byte[] salt;

    /** Whether or not the login information is all valid **/
    private boolean valid = false;


    /**
     * Constructor for new login information
     *
     * @param username user's username
     * @param password user's un-hashed password
     */
    public Login(String username, String password) {
        // Check that username and password are valid.
        if (!Security.validateUsername(username)) {
            Log.e("Login", "Invalid username: " + username);
            return;
        } else {
            this.username = username;
        }
        if (!Security.validatePassword(password)) {
            Log.e("Login", "Invalid password");
            return;
        }
        // Create secure password
        salt = Security.generateSalt();
        securePassword = Security.createEncryptedPassword(password, salt);
        if (securePassword != null && salt != null) {
            valid = true;
        }
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
     * @return encrypted password
     */
    public byte[] getSecurePassword() {
        return securePassword;
    }


    /**
     *
     * @return salt used to encrypt login password
     */
    public byte[] getSalt() {
        return salt;
    }


    /**
     *
     * @return true if login data is valid, false if not
     */
    public boolean isValid() {
        return valid;
    }


    /**
     *
     * @return Login information as a string
     */
    public String toString() {
        return String.format("Username: %s\nEncrypted Password: %s\n", username, Security.getPasswordString(securePassword));
    }
}
