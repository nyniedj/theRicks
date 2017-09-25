package edu.gatech.cs2340.thericks.utils;

import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.gatech.cs2340.thericks.models.Login;

/**
 * Created by Ben Lashley on 9/21/2017.
 *
 * Class handles security for login system, including password encryption and login validation.
 */

public class Security {

    /** Number of iterations for password encryption **/
    private static final int ITERATIONS = 20000;

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
     * Generates a secure, random salt for encryption.
     * @return salt
     */
    public static byte[] generateSalt() {
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
     * @return encrypted key as byte[]
     */
    public static byte[] createEncryptedPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH * 8);
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            Log.e("Login", "Algorithm requested for encryption does not exist.");
            e.printStackTrace();
        }
        if (f == null) {
            return null;
        }
        try {
            return f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            Log.e("Login", "Invalid specifications for secure key.");
            e.printStackTrace();
        }
        Log.e("Login", "Failed to create secure key.");
        return null;
    }


    /**
     * Checks if a new user's desired username is valid.
     *
     * @param username the desired username
     * @return true if valid, false if not
     */
    public static boolean validateUsername(String username) {
        // Check if username is valid
        if (username == null) {
            return false;
        } //else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
          //  return false;
        //}     uncomment these lines after m4
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
    public static boolean validatePassword(String pw) {
        //final Pattern pat = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{"+ MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "}$");
        //return pw != null && pat.matcher(pw).matches();
        return true; //uncomment after m4
    }

    /**
     * Check if provided password gives correct encryption (i.e. password entered is correct)
     *
     * @param pw the password to check
     * @return true if the password is correct, false if not
     */
    public static boolean checkPassword(String pw, Login login) {
        // Check that pw is valid
        if (!validatePassword(pw)) {
            return false;
        }
        byte[] attempt = createEncryptedPassword(pw, login.getSalt());

        Log.d("Login", "Attempted password encrypted to: " + getPasswordString(attempt));
        Log.d("Login", "Actual encrypted password: " + getPasswordString(login.getSecurePassword()));

        return Arrays.equals(attempt, login.getSecurePassword());
    }

    /**
     * NOTE: Only needed for testing purposes.  Responsibility of printing password is with Login.
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
}
