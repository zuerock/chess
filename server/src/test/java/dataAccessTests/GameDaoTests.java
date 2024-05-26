package dataAccessTests;

import dataAccess.interfaces.GameDao;
import dataAccess.sql.SQLGameDao;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

public class GameDaoTests {
    static GameDao gameDao;

    @BeforeAll
    public static void setup() {
        try {
            gameDao = SQLGameDao.getInstance();
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @BeforeEach
    public void cleanup() {
        gameDao.clear();
    }

    @Test
    public void testCreateGamePositive() {
        Set<GameData> empty = new HashSet<>();
        Set<GameData> notEmpty = new HashSet<>();
        GameData data = new GameData(1234, null, null, null, null);
        notEmpty.add(data);

        Assertions.assertEquals(empty, gameDao.listGames());
        gameDao.createGame(data);
        Assertions.assertEquals(notEmpty, gameDao.listGames());
    }

    @Test
    public void testCreateGameNegative() {
        Set<GameData> empty = new HashSet<>();
        GameData invalidData = new GameData(null, null, null, null, null);

        Assertions.assertEquals(empty, gameDao.listGames());
        gameDao.createGame(invalidData);
        Assertions.assertEquals(empty, gameDao.listGames());
    }

    @Test
    public void testGetGamePositive() {
        GameData data = new GameData(1234, null, null, null, null);
        gameDao.createGame(data);

        Assertions.assertNotNull(gameDao.getGame(data));
    }

    @Test
    public void testGetGameNegative() {
        GameData data = new GameData(1234, null, null, null, null);

        Assertions.assertNull(gameDao.getGame(data));
    }

    @Test
    public void testListGamesPositive() {
        GameData data1 = new GameData(1234, null, null, null, null);
        GameData data2 = new GameData(5678, null, null, null, null);
        Set<GameData> notEmpty = new HashSet<>();
        notEmpty.add(data1);
        notEmpty.add(data2);
        gameDao.createGame(data1);
        gameDao.createGame(data2);

        Assertions.assertEquals(notEmpty, gameDao.listGames());
    }

    @Test
    public void testListGamesNegative() {
        Set<GameData> empty = new HashSet<>();
        Assertions.assertEquals(empty, gameDao.listGames());
    }

    @Test
    public void testUpdateGamePositive() {
        GameData data = new GameData(1234, null, null, null, null);
        GameData newData = new GameData(1234, "", "", "", null);
        gameDao.createGame(data);

        Assertions.assertEquals(data, gameDao.getGame(data));
        gameDao.updateGame(newData);
        Assertions.assertNotEquals(data, gameDao.getGame(data));
    }

    @Test
    public void testUpdateGameNegative() {
        GameData data = new GameData(1234, null, null, null, null);
        GameData newData = new GameData(5678, "", "", "", null);
        gameDao.createGame(data);

        Assertions.assertEquals(data, gameDao.getGame(data));
        gameDao.updateGame(newData);
        Assertions.assertEquals(data, gameDao.getGame(data));
    }

    @Test
    public void testClearPositive() {
        Set<GameData> empty = new HashSet<>();
        GameData data = new GameData(1234, null, null, null, null);
        gameDao.createGame(data);

        Assertions.assertNotEquals(empty, gameDao.listGames());
        gameDao.clear();
        Assertions.assertEquals(empty, gameDao.listGames());
    }
}
