package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void clear() throws IOException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Nested
    class RegisterTests {

        @Test
        void goodRegister() throws Exception {
            AuthData authData = registerSetup("testUser", "testPassword", "testEmail");
            Assertions.assertTrue(authData.authToken().length() > 10);
        }

        @Test
        void badRegister() {
            Assertions.assertThrows(IOException.class, () -> registerSetup(null, "testPassword", "badEmail"));
        }
    }

    @Nested
    class LoginTests {

        @Test
        void goodLogin() throws Exception {
            AuthData authData = registerSetup("testUser", "testPassword", "testEmail");

            AuthData loginData = facade.login("testUser", "testPassword");
            Assertions.assertEquals(authData.username(), loginData.username());
        }

        @Test
        void badLogin() throws Exception {
            registerSetup("testUser", "testPassword", "testEmail");

            Assertions.assertThrows(IOException.class, () -> facade.login("testUser", "badPassword"));
        }
    }

    @Nested
    class LogoutTests {

        @Test
        void goodLogout() throws Exception {
            AuthData authData = registerSetup("testUser", "testPassword", "testEmail");

            int responseCode = facade.logout(authData.authToken());
            assertEquals(200, responseCode);
        }

        @Test
        void badLogout() {
            Assertions.assertThrows(IOException.class, () -> facade.logout("badAuthToken"));
        }
    }






    AuthData registerSetup(String username, String password, String email) throws IOException {
        AuthData auth =  facade.register(username, password, email);
        Assertions.assertNotNull(auth, "Registration failed");
        return auth;
    }
}
