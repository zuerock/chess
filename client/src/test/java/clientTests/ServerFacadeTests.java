package clientTests;

import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;
import server.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private static RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
    private static LoginRequest loginRequest = new LoginRequest("user", "pass");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void cleanup() {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        facade.clear();
        server.stop();
    }


    @Test
    public void clearTestPositive() {
        facade.register(registerRequest);

        AuthResult authResult = facade.login(loginRequest);
        Assertions.assertNotNull(authResult.authToken());
        facade.clear();
        authResult = facade.login(loginRequest);
        Assertions.assertNull(authResult.authToken());
    }

    @Test
    public void createGameTestPositive() {
        AuthResult authResult = facade.register(registerRequest);
        String authToken = authResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("name", authToken);
        GameResult gameResult = facade.createGame(createGameRequest);
        Assertions.assertNull(gameResult.message());
    }

    @Test
    public void createGameTestNegative() {
        facade.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("name", "notAuthToken");
        GameResult gameResult = facade.createGame(createGameRequest);
        Assertions.assertNotNull(gameResult.message());
    }

    @Test
    public void joinGameTestPositive() {
        AuthResult authResult = facade.register(registerRequest);
        String authToken = authResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("name", authToken);
        GameResult gameResult = facade.createGame(createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameResult.gameID(), authToken);
        Result result = facade.joinGame(joinGameRequest);
        Assertions.assertNull(result.message());
    }

    @Test
    public void joinGameTestNegative() {
        AuthResult authResult = facade.register(registerRequest);
        String authToken = authResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("name", authToken);
        GameResult gameResult = facade.createGame(createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameResult.gameID(), "notAuthToken");
        Result result = facade.joinGame(joinGameRequest);
        Assertions.assertNotNull(result.message());
    }

    @Test
    public void listGamesTestPositive() {
        AuthResult authResult = facade.register(registerRequest);
        String authToken = authResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("name", authToken);
        facade.createGame(createGameRequest);
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        GameListResult gameListResult = facade.listGames(listGamesRequest);
        Assertions.assertNull(gameListResult.message());
    }

    @Test
    public void listGamesTestNegative() {
        AuthResult authResult = facade.register(registerRequest);
        String authToken = authResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("name", authToken);
        facade.createGame(createGameRequest);
        ListGamesRequest listGamesRequest = new ListGamesRequest("notAuthToken");
        GameListResult gameListResult = facade.listGames(listGamesRequest);
        Assertions.assertNotNull(gameListResult.message());
    }

    @Test
    public void loginTestPositive() {
        facade.register(registerRequest);

        AuthResult authResult = facade.login(loginRequest);
        Assertions.assertNull(authResult.message());
    }

    @Test
    public void loginTestNegative() {
        AuthResult authResult = facade.login(loginRequest);
        Assertions.assertNotNull(authResult.message());
    }

    @Test
    public void logoutTestPositive() {
        AuthResult authResult = facade.register(registerRequest);
        String authToken = authResult.authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        Result result = facade.logout(logoutRequest);
        Assertions.assertNull(result.message());
    }

    @Test
    public void logoutTestNegative() {
        facade.register(registerRequest);

        LogoutRequest logoutRequest = new LogoutRequest("notAuthToken");
        Result result = facade.logout(logoutRequest);
        Assertions.assertNotNull(result.message());
    }

    @Test
    public void registerTestPositive() {
        AuthResult authResult = facade.register(registerRequest);
        Assertions.assertNull(authResult.message());
    }

    @Test
    public void registerTestNegative() {
        AuthResult authResult = facade.register(new RegisterRequest(null, null, null));
        Assertions.assertNotNull(authResult.message());
    }
}
