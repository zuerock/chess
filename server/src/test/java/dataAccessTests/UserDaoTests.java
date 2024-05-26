package dataAccessTests;

import dataAccess.interfaces.UserDao;
import dataAccess.sql.SQLUserDao;
import model.UserData;
import org.junit.jupiter.api.*;

public class UserDaoTests {

    static UserDao userDao;

    @BeforeAll
    public static void setup() {
        try {
            userDao = SQLUserDao.getInstance();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @BeforeEach
    public void cleanup() {
        userDao.clear();
    }

    @Test
    public void testCreateUserPositive() {
        UserData data = new UserData("user", "pass", "email");

        Assertions.assertNull(userDao.getUser(data));
        userDao.createUser(data);
        Assertions.assertNotNull(userDao.getUser(data));
    }

    @Test
    public void testCreateUserNegative() {
        UserData invalidData = new UserData(null, "pass", "email");

        Assertions.assertNull(userDao.getUser(invalidData));
        userDao.createUser(invalidData);
        Assertions.assertNull(userDao.getUser(invalidData));
    }

    @Test
    public void testGetUserPositive() {
        UserData data = new UserData("user", "pass", "email");
        userDao.createUser(data);

        Assertions.assertEquals(data, userDao.getUser(data));
    }

    @Test
    public void testGetUserNegative() {
        UserData data = new UserData("user", "pass", "email");

        Assertions.assertNull(userDao.getUser(data));
    }

    @Test
    public void testClearPositive() {
        UserData data = new UserData("user", "pass", "email");
        userDao.createUser(data);

        Assertions.assertNotNull(userDao.getUser(data));
        userDao.clear();
        Assertions.assertNull(userDao.getUser(data));
    }
}
