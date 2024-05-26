package serviceTests;

import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.*;
import org.junit.jupiter.api.*;
import request.JoinGameRequest;
import service.*;

public class JoinGameServiceTests {
    private int gameID = 1000;
    private GameData gameData = new GameData(gameID, null, null, "name", null);

    @BeforeEach
    public void setup() {
        try {
            GameDao gameDao = SQLGameDao.getInstance();

            ClearService clearService = ClearService.getInstance();

            clearService.clear();

            AuthDao authDao = SQLAuthDao.getInstance();
            gameDao.createGame(gameData);
            authDao.createAuth(new AuthData("auth", "user"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testJoinGamePositive() {
        try {
            GameDao gameDao = SQLGameDao.getInstance();

            Assertions.assertNull(gameDao.getGame(gameData).whiteUsername());

            JoinGameService joinGameService = JoinGameService.getInstance();
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID, "auth");
            joinGameService.joinGame(joinGameRequest);

            Assertions.assertNotNull(gameDao.getGame(gameData).whiteUsername());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testJoinGameNegative() {
        try {
            GameDao gameDao = SQLGameDao.getInstance();

            Assertions.assertNull(gameDao.getGame(gameData).whiteUsername());

            JoinGameService joinGameService = JoinGameService.getInstance();
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID, "notAuth");

            joinGameService.joinGame(joinGameRequest);

            Assertions.assertNull(gameDao.getGame(gameData).whiteUsername());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
