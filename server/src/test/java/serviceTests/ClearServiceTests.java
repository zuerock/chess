package serviceTests;

import dataAccess.interfaces.UserDao;
import dataAccess.sql.*;
import org.junit.jupiter.api.*;
import model.*;
import service.*;

public class ClearServiceTests {
    @Test
    public void testPositiveClear() {
        try {
            UserDao userDao = SQLUserDao.getInstance();
            UserData userData = new UserData("user", "pass", "email");
            userDao.createUser(userData);

            Assertions.assertNotNull(userDao.getUser(userData));

            ClearService service = ClearService.getInstance();

            service.clear();

            Assertions.assertNull(userDao.getUser(userData));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
