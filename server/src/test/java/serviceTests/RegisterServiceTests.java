package serviceTests;

import org.junit.jupiter.api.*;
import request.RegisterRequest;
import service.*;

public class RegisterServiceTests {
    @BeforeEach
    public void setup() {
        ClearService clearService = ClearService.getInstance();
        try {
            clearService.clear();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testLogoutPositive() {
        RegisterService registerService = RegisterService.getInstance();
        RegisterRequest registerRequest = new RegisterRequest("user","pass","email");
        try {
            Assertions.assertNull(registerService.register(registerRequest).message());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testLogoutNegative() {
        RegisterService registerService = RegisterService.getInstance();
        RegisterRequest registerRequest = new RegisterRequest(null,null,null);
        try {
            Assertions.assertNotNull(registerService.register(registerRequest).message());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
