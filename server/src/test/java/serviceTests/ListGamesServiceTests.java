package serviceTests;

import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import request.ListGamesRequest;
import service.ClearService;
import service.ListGamesService;

import java.util.HashSet;
import java.util.Set;

public class ListGamesServiceTests {
    @BeforeAll
    public static void setup() {
        try {
            ClearService clearService = ClearService.getInstance();
            clearService.clear();
            GameDao gameDao = SQLGameDao.getInstance();
            gameDao.createGame(new GameData(1234, null, null, null, null));
            AuthDao authDao = SQLAuthDao.getInstance();
            authDao.createAuth(new AuthData("auth", "user"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testListGamesPositive() {
        try {
            ListGamesService listGamesService = ListGamesService.getInstance();
            ListGamesRequest listGamesRequest = new ListGamesRequest("auth");

            Set<GameData> set = new HashSet<>();
            set.add(new GameData(1234, null,null, null, null));

            Assertions.assertEquals(set, listGamesService.listGames(listGamesRequest).games());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testListGamesNegative() {
        try {
            ListGamesService listGamesService = ListGamesService.getInstance();
            ListGamesRequest listGamesRequest = new ListGamesRequest("notAuth");

            Assertions.assertNull(listGamesService.listGames(listGamesRequest).games());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
