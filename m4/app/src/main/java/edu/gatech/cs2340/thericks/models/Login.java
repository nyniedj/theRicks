package edu.gatech.cs2340.thericks.models;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;

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

    public boolean resetPassword(String oldPass, String newPass) {
        if (!Security.validatePassword(newPass)) {
            Log.d("Login", "Failed attempt to reset password. Invalid new password.");
            return false;
        }
        if (Security.checkPassword(oldPass, this)) {
            // Old password was entered correctly, so allow reset.
            byte[] fallBack = securePassword;
            securePassword = Security.createEncryptedPassword(newPass, salt);
            if (securePassword == null) {
                // Reset failed to create encrypted new password
                securePassword = fallBack;
                Log.e("Login", "Error resetting password. Failed to encrypt new password. Password is unchanged.");
                return false;
            } else {
                // Encryption successful
                return true;
            }
        } else {
            Log.d("Login", "Failed attempt to reset password. Incorrect old password.");
            return false;
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
     * @return secure password
     */
    public byte[] getSecurePassword() {
        return securePassword;
    }


    /**
     *
     * @return Secure password as a readable hexadecimal string
     */
    public String getPasswordString() {
        if (securePassword == null) {
            return "INVALID";
        }
        final StringBuilder builder = new StringBuilder();
        for (byte b : securePassword) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
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
        return String.format("Username: %s\nEncrypted Password: %s\n", username, getPasswordString());
    }
}
