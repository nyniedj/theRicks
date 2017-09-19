package edu.gatech.cs2340.thericks;

import org.junit.Test;

import edu.gatech.cs2340.thericks.models.*;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginSystemUnitTest {

    @Test
    public void test_login() throws Exception {
        User u1 = new User("Bobby123", "tacosRgood");
        User u2 = new User("Lindsay99", "tacosRgo0d");   // check that similar passwords are completely different when encrypted
        User u3 = new User("JillPickle", "tacosRgood");  // check that salt works with same password

        System.out.println(u1.getLogin());
        System.out.println(u2.getLogin());
        System.out.println(u3.getLogin());

        assertNotNull(u1.getLogin().getSecurePassword());
        assertNotNull(u2.getLogin().getSecurePassword());
        assertNotNull(u3.getLogin().getSecurePassword());
    }

    @Test
    public void test_password_validation() throws Exception {
        UserTable users = new UserTable();

        // Check invalid passwords
        assertFalse(users.addUserFromData("username1", "173839304"));
        assertFalse(users.addUserFromData("username2", "short"));
        assertFalse(users.addUserFromData("username3", "white space"));
        assertFalse(users.addUserFromData("username4", "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong"));
        assertFalse(users.addUserFromData("username5", "all_lowercase"));
        assertFalse(users.addUserFromData("username6", "bad_chars^^<{}>"));
        assertFalse(users.addUserFromData("username7", null));

        // Check valid passwords
        assertTrue(users.addUserFromData("username1", "Pa@ssW0rd"));
        assertTrue(users.addUserFromData("username2", "A78b37?@Jfd"));
        assertTrue(users.addUserFromData("username3", "L3tMeInPlz"));

    }
}