package serviceTests;

import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.*;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import service.*;

import java.util.HashSet;

public class CreateGameServiceTests {
    @BeforeEach
    public void clear() {
        ClearService service = ClearService.getInstance();
        try {
            service.clear();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    @Test
    public void testCreateGamePositive() {
        try {
            GameDao gameDao = SQLGameDao.getInstance();
            AuthDao authDao = SQLAuthDao.getInstance();
            String authToken = "auth";
            authDao.createAuth(new AuthData(authToken, "user"));

            Assertions.assertEquals(new HashSet<GameData>(), gameDao.listGames());

            CreateGameService createGameService = CreateGameService.getInstance();
            CreateGameRequest createGameRequest = new CreateGameRequest("name", authToken);

            createGameService.createGame(createGameRequest);

            Assertions.assertNotEquals(new HashSet<GameData>(), gameDao.listGames());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testCreateGameNegative() {
        try {
            GameDao gameDao = SQLGameDao.getInstance();
            AuthDao authDao = SQLAuthDao.getInstance();
            String authToken = "auth";
            authDao.createAuth(new AuthData(authToken, "user"));

            Assertions.assertEquals(new HashSet<GameData>(), gameDao.listGames());

            CreateGameService createGameService = CreateGameService.getInstance();
            CreateGameRequest createGameRequest = new CreateGameRequest("name", "notAuth");

            createGameService.createGame(createGameRequest);

            Assertions.assertEquals(new HashSet<GameData>(), gameDao.listGames());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
