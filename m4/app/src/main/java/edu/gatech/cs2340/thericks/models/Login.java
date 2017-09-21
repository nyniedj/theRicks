package edu.gatech.cs2340.thericks.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.security.Security;
import java.util.Arrays;
import java.util.regex.Pattern;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

    /** Number of iterations for password encryption **/
    private static final int ITERATIONS = 200000;

    /** Size in bytes of salt **/
    private static final int SALT_LENGTH = 16;

    /** Size in bytes of encrypted key **/
    private static final int KEY_LENGTH = 32;

    /** See validatePassword() for complete password criteria **/
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 32;

    private static final int MIN_USERNAME_LENGTH = 6;
    private static final int MAX_USERNAME_LENGTH = 32;


    /**
     * Constructor for new login information
     *
     * @param username user's username
     * @param password user's un-hashed password
     */
    public Login(String username, String password) {
        // Check that username and password are valid.
        if (!validateUsername(username)) {
            Log.e("Login", "Invalid username: " + username);
            return;
        }
        else if (!validatePassword(password)) {
            Log.e("Login", "Invalid password");
            return;
        }

        // Good username/password
        this.username = username;
        salt = generateSalt();

        try {
            securePassword = createEncryptedPassword(password, salt, ITERATIONS, KEY_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Login", "Algorithm requested for encryption does not exist.");
        } catch (InvalidKeySpecException e) {
            Log.e("Login", "Invalid specifications for secure key.");
        }

        if (securePassword == null) {
            // Password encryption failed
            Log.e("Login", "User's encrypted password could not be created.");
        } else {
            // Password encryption succeeded
            valid = true;
        }
    }

    /**
     * Generates a secure, random salt for encryption.
     * @return salt
     */
    private byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        sr.nextBytes(saltBytes);
        return saltBytes;
    }

    /**
     * Creates encrypted password from user's password and random salt.
     *
     * @param password user's (unencrypted) password
     * @param salt randomly generated salt
     * @param iterations number of iterations
     * @param derivedKeyLength length of encrypted key in bytes
     * @return encrypted key as byte[]
     * @throws NoSuchAlgorithmException if requested algorithm for getting key factory does not exist.
     * @throws InvalidKeySpecException if key specifications are invalid
     */
    private static byte[] createEncryptedPassword(String password, byte[] salt,  int iterations, int derivedKeyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength * 8);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }

    /**
     * Checks if a new user's desired username is valid.
     *
     * @param username the desired username
     * @return true if valid, false if not
     */
    private boolean validateUsername(String username) {
        // Check if username is valid
        if (username == null) {
            return false;
        } else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * Checks if password meets criteria to be considered valid.
     * Criteria (must meet all):
     *  (?=.*[0-9]) at least one digit
     *  (?=.*[a-z]) at least one lowercase letter
     *  (?=.*[A-Z]) at least one uppercase letter
     *  (?=\\S+$)   no whitespace
     *  .{MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH}     length between min and max (inclusive)
     *
     * @param pw the password to validate
     * @return true if password is valid, false if not
     */
    private boolean validatePassword(String pw) {
        if (pw == null) {
            return false;
        }
        final Pattern pat = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{"+ MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "}$");
        return pw != null && pat.matcher(pw).matches();
    }

    /**
     * Check if provided password gives correct encryption (i.e. password entered is correct)
     *
     * @param pw the password to check
     * @return true if the password is correct, false if not
     */
    public boolean checkPassword(String pw) {
        // Make sure Login info is valid
        if (!isValid()) {
            return false;
        }
        // Check that pw is valid
        if (!validatePassword(pw)) {
            return false;
        }
        boolean correct = false;
        try {
            byte[] attempt = createEncryptedPassword(pw, this.salt, ITERATIONS, KEY_LENGTH);
            Log.d("Login", "Attempted password encrypted to: " + getPasswordString(attempt));
            Log.d("Login", "Actual encrypted password: " + getPasswordString(securePassword));
            correct = Arrays.equals(attempt, securePassword);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Login", "Algorithm requested for encryption does not exist.");
        } catch (InvalidKeySpecException e) {
            Log.e("Login", "Invalid specifications for secure key.");
        }
        return correct;
    }

    /**
     * Resets current password to new password. If new password is invalid, nothing is changed.
     *
     * @param newPassword the new password to be set
     * @return true if reset was successful, false if not
     */
    public boolean resetPassword(String newPassword) {
        if (!validatePassword(newPassword)) {
            return false;
        }
        salt = generateSalt();
        try {
            securePassword = createEncryptedPassword(newPassword, salt, ITERATIONS, KEY_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Login", "Algorithm requested for encryption does not exist.");
            return false;
        } catch (InvalidKeySpecException e) {
            Log.e("Login", "Invalid specifications for secure key.");
            return false;
        }

        if (securePassword == null) {
            // Password encryption failed
            Log.e("Login", "User's encrypted password could not be created.");
            return false;
        } else {
            // Password encryption succeeded
            valid = true;
            return true;
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
     * @return Secure password as a readable hexadecimal string
     */
    public static String getPasswordString(byte[] pw) {
        if (pw == null) {
            return "INVALID";
        }
        final StringBuilder builder = new StringBuilder();
        for (byte b : pw) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
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
        return String.format("Username: %s\nEncrypted Password: %s\n", username, getPasswordString(securePassword));
    }
}
