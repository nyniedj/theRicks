package edu.gatech.cs2340.thericks;

import org.junit.Test;

import edu.gatech.cs2340.thericks.models.*;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

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
}