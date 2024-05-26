package dataAccessTests;

import dataAccess.interfaces.AuthDao;
import dataAccess.sql.SQLAuthDao;
import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDaoTests {

    static AuthDao authDao;

    @BeforeAll
    public static void setup() {
        try {
            authDao = SQLAuthDao.getInstance();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @BeforeEach
    public void cleanup() {
        authDao.clear();
    }

    @Test
    public void testCreateAuthPositive() {
        AuthData data = new AuthData("auth", "user");

        Assertions.assertNull(authDao.getUser(data));
        authDao.createAuth(data);
        Assertions.assertNotNull(authDao.getUser(data));
    }

    @Test
    public void testCreateAuthNegative() {
        AuthData invalidData = new AuthData(null, "user");

        Assertions.assertNull(authDao.getUser(invalidData));
        authDao.createAuth(invalidData);
        Assertions.assertNull(authDao.getUser(invalidData));
    }

    @Test
    public void testGetUserPositive() {
        AuthData data = new AuthData("auth", "user");
        authDao.createAuth(data);

        Assertions.assertEquals(data, authDao.getUser(data));
    }

    @Test
    public void testGetUserNegative() {
        AuthData data = new AuthData("auth", "user");

        Assertions.assertNull(authDao.getUser(data));
    }

    @Test
    public void testDeleteAuthPositive() {
        AuthData data = new AuthData("auth", "user");
        authDao.createAuth(data);

        Assertions.assertNotNull(authDao.getUser(data));
        authDao.deleteAuth(data);
        Assertions.assertNull(authDao.getUser(data));
    }

    @Test
    public void testDeleteAuthNegative() {
        AuthData data1 = new AuthData("auth", "user");
        AuthData data2 = new AuthData("notAuth", "notUser");
        authDao.createAuth(data1);

        Assertions.assertNotNull(authDao.getUser(data1));
        authDao.deleteAuth(data2);
        Assertions.assertNotNull(authDao.getUser(data1));
    }

    @Test
    public void testClearPositive() {
        AuthData data = new AuthData("auth", "user");
        authDao.createAuth(data);

        Assertions.assertNotNull(authDao.getUser(data));
        authDao.clear();
        Assertions.assertNull(authDao.getUser(data));
    }
}
