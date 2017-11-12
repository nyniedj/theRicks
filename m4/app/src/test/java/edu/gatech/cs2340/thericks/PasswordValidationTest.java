package edu.gatech.cs2340.thericks;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.gatech.cs2340.thericks.utils.Security;

/**
 * Unit tests for validating passwords.
 *
 * Criteria for valid password (must meet all):
 *  (?=.*[0-9]) at least one digit
 *  (?=.*[a-z]) at least one lowercase letter
 *  (?=.*[A-Z]) at least one uppercase letter
 *  (?=\\S+$)   no whitespace
 *  .{MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH}     length between min (8) and max (32) (inclusive)
 *
 * Created by Ben Lashley on 11/3/2017.
 */

public class PasswordValidationTest {

    // Helper method to shorten test method calls
    private boolean test(String pw) {
        return Security.validatePassword(pw);
    }

    @Test
    public void testNullPassword() {
        assertFalse("Null password is considered valid, should be invalid.", test(null));
    }

    @Test
    public void testTooShortPassword() {
        String pw1 = "";
        String pw2 = "aA1";
        String pw3 = "aaaAAA1"; // size = 7

        String failMessage = "Too short password is considered valid, should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
    }

    @Test
    public void testTooLongPassword() {
        String pw1 = "aA1111111111111111111111111111111"; // size = 33
        String pw2 = pw1 + pw1; // size = 66

        String failMessage = "Too long password is considered valid, should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
    }

    @Test
    public void testMaxAllowedLength() {
        String pw1 = "aA111111111111111111111111111111"; // size = 32
        assertTrue("Valid password of max length is considered invalid", test(pw1));
    }

    @Test
    public void testMinAllowedLength() {
        String pw1 = "aA111111"; // size = 8
        assertTrue("Valid password of min length is considered invalid", test(pw1));
    }

    @Test
    public void testPasswordHasWhitespace() {
        String pw1 = "hiTh3re World";
        String pw2 = "h3resATab\t";
        String pw3 = "AndAN3wLine\n";
        String pw4 = "\n\t l0tsOfWhitespace";

        String failMessage = "A password containing whitespace is considered valid, should be invalid";
        assertFalse(failMessage, test(pw1));
        assertFalse(failMessage, test(pw2));
        assertFalse(failMessage, test(pw3));
        assertFalse(failMessage, test(pw4));
    }
}
