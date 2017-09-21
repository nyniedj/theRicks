package edu.gatech.cs2340.thericks;

import org.junit.Test;

import java.security.Security;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import edu.gatech.cs2340.thericks.models.*;

import static org.junit.Assert.*;

/**
 * Unit tests for the login system.
 */
public class LoginSystemUnitTest {

    @Test
    public void create_user() throws Exception {
        User u1 = new User("Bobby123", "tacosRg00d");
        User u2 = new User("Lindsay99", "tacosRgo0d");   // check that similar passwords are completely different when encrypted
        User u3 = new User("JillPickle", "tacosRgo0d");  // check that salt works with same password

        System.out.println(u1.getLogin());
        System.out.println(u2.getLogin());
        System.out.println(u3.getLogin());

        assertTrue(u1.getLogin().isValid());
        assertTrue(u2.getLogin().isValid());
        assertTrue(u3.getLogin().isValid());
    }

    @Test
    public void password_validation() throws Exception {
        UserTable users = UserTable.getInstance();

        // Check invalid passwords
        assertFalse(users.addUserFromData("username1", "173839304"));
        assertFalse(users.addUserFromData("username2", "short"));
        assertFalse(users.addUserFromData("username3", "white space"));
        assertFalse(users.addUserFromData("username4", "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong"));
        assertFalse(users.addUserFromData("username5", "all_lowercase"));
        assertFalse(users.addUserFromData("username6", "bad_chars^^<{}>"));
        assertFalse(users.addUserFromData("username7", null));

        // Check valid passwords
        assertTrue(users.addUserFromData("username1", "P@ssW0rd"));
        assertTrue(users.addUserFromData("username2", "A78b37?@Jfd"));
        assertTrue(users.addUserFromData("username3", "L3tMeInPlz"));

        // Check that users are now in user table
        assertNotNull(users.getUserByUsername("username1"));
        assertNotNull(users.getUserByUsername("username2"));
        assertNotNull(users.getUserByUsername("username3"));
    }

    @Test
    public void reset_password() throws Exception {
        User u1 = new User("username1", "P@ssW0rd");
        System.out.println(u1.getLogin());

        // Check valid reset works
        u1.getLogin().resetPassword("P@ssW0rd2ElectricBoogaloo");
        System.out.println(u1.getLogin());
        assertTrue(u1.getLogin().isValid());

        // Check that invalid reset attempt does nothing
        u1.getLogin().resetPassword("Bad Password");
        System.out.println(u1.getLogin());
        assertTrue(u1.getLogin().isValid());  // Should still be valid since nothing changed

        // Check that resetting to same password still resets salt (i.e. encryption password changes)
        u1.getLogin().resetPassword("P@ssW0rd2ElectricBoogaloo");
        System.out.println(u1.getLogin());
        assertTrue(u1.getLogin().isValid());
    }
}