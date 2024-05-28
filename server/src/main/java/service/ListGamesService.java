package service;

import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.AuthData;
import model.GameData;
import request.ListGamesRequest;
import result.GameListResult;

import java.util.Set;

public class ListGamesService {
    private static ListGamesService instance;

    private ListGamesService() {}

    public static ListGamesService getInstance() {
        if (instance == null) {
            instance = new ListGamesService();
        }
        return instance;
    }

    public GameListResult listGames(ListGamesRequest request) throws DataAccessException {
        AuthDao authDao = SQLAuthDao.getInstance();
        String authToken = request.authToken();
        AuthData authData = new AuthData(authToken, null);
        authData = authDao.getUser(authData);
        if(authData == null) {
            return new GameListResult(null, "Error: unauthorized");
        }

        GameDao gameDao = SQLGameDao.getInstance();
        Set<GameData> games = gameDao.listGames();

        return new GameListResult(games, null);
    }
}
