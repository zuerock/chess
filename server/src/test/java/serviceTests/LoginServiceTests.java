package serviceTests;

import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import service.*;

public class LoginServiceTests {
    @BeforeAll
    public static void setup() {
        RegisterService registerService = RegisterService.getInstance();
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
        try {
            registerService.register(registerRequest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testLoginPositive() {
        LoginService loginService = LoginService.getInstance();
        LoginRequest loginRequest = new LoginRequest("user", "pass");
        try {
            Assertions.assertNotNull(loginService.login(loginRequest).authToken());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testLoginNegative() {
        LoginService loginService = LoginService.getInstance();
        LoginRequest loginRequest = new LoginRequest("user", "notPass");
        try {
            Assertions.assertNull(loginService.login(loginRequest).authToken());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
