package edu.gatech.cs2340.thericks;

import org.junit.Test;

import edu.gatech.cs2340.thericks.models.*;
import edu.gatech.cs2340.thericks.utils.Security;

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
        assertFalse(users.addUserFromData(null, null));

        assertEquals(users.size(), 0);

        // Check valid passwords
        assertTrue(users.addUserFromData("username1", "P@ssW0rd"));
        assertTrue(users.addUserFromData("username2", "A78b37?@Jfd"));
        assertTrue(users.addUserFromData("username3", "L3tMeInPlz"));

        // Check that users are now in user table
        assertEquals(users.size(), 3);
        assertNotNull(users.getUserByUsername("username1"));
        assertNotNull(users.getUserByUsername("username2"));
        assertNotNull(users.getUserByUsername("username3"));
    }

    @Test
    public void reset_password() {
        User u1 = new User("Username", "P@ssw0rd");
        System.out.println("Old password: " + u1.getLogin().getPasswordString());

        assertFalse(u1.getLogin().resetPassword("P@ssword!", "Op3nSesame"));
        assertFalse(u1.getLogin().resetPassword("P@ssword", "invalid"));

        assertTrue(u1.getLogin().resetPassword("P@ssw0rd", "Op3nSesame"));

        System.out.println("New Password: " + u1.getLogin().getPasswordString());
        assertArrayEquals(Security.createEncryptedPassword("Op3nSesame", u1.getLogin().getSalt()), u1.getLogin().getSecurePassword());
    }
}