package serviceTests;

import org.junit.jupiter.api.*;
import request.*;
import service.*;

public class LogoutServiceTests {
    private static String authToken;
    @BeforeAll
    public static void setup() {
        RegisterService registerService = RegisterService.getInstance();
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
        try {
            authToken = registerService.register(registerRequest).authToken();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testLogoutPositive() {
        LogoutService logoutService = LogoutService.getInstance();
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            Assertions.assertNull(logoutService.logout(logoutRequest).message());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testLogoutNegative() {
        LogoutService logoutService = LogoutService.getInstance();
        LogoutRequest logoutRequest = new LogoutRequest("notAuth");
        try {
            Assertions.assertNotNull(logoutService.logout(logoutRequest).message());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
