package edu.gatech.cs2340.thericks.models;

import android.util.Log;

import java.io.Serializable;

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

    /** Number of iterations for password encryption **/
    private static final int ITERATIONS = 200000;

    /** Size in bytes of salt **/
    private static final int SALT_LENGTH = 16;

    /** Size in bytes of encrypted key **/
    private static final int KEY_LENGTH = 32;


    /**
     * Constructor for login info
     *
     * @param username user's username
     * @param password user's un-hashed password
     */
    public Login(String username, String password) {
        this.username = username;
        salt = generateSalt();

        try {
            securePassword = createEncryptedPassword(password, salt, ITERATIONS, KEY_LENGTH);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Login", "Algorithm requested for encryption does not exist.");
        } catch (InvalidKeySpecException e) {
            Log.e("Login", "Invalid specifications for secure key.");
        }

        /* Password encryption failed. Invalidate login info. */
        if (securePassword == null) {
            salt = null;
            Log.e("Login", "User login info could not be created.");
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
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return f.generateSecret(spec).getEncoded();
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
     * @return salt used to encrypt password
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     *
     * @return Secure password as a readable hexadecimal string
     */
    private String getPasswordString() {
        final StringBuilder builder = new StringBuilder();
        for (byte b : securePassword) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /**
     *
     * @return Login information as a string
     */
    public String toString() {
        return String.format("Username: %s\nEncrypted Password: %s\n", username, getPasswordString());
    }
}
