package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.*;
import request.CreateGameRequest;
import result.GameResult;

import java.util.Random;

public class CreateGameService {
    private static CreateGameService instance;

    private CreateGameService() {}

    public static CreateGameService getInstance() {
        if (instance == null) {
            instance = new CreateGameService();
        }
        return instance;
    }

    public GameResult createGame(CreateGameRequest request) throws DataAccessException {
        if (request.gameName() == null) {
            return new GameResult(null, null, null, null,
                    "Error: bad request");
        }
        AuthDao authDao = SQLAuthDao.getInstance();
        String authToken = request.authToken();
        AuthData authData = new AuthData(authToken, null);
        authData = authDao.getUser(authData);
        if(authData == null) {
            return new GameResult(null, null, null, null,
                    "Error: unauthorized");
        }

        GameDao gameDao = SQLGameDao.getInstance();

        String gameName = request.gameName();
        GameData gameData;
        do { //this method is only effective if a few games are being played, and maxes out at 9000 total games
            Random random = new Random();
            int gameID = random.nextInt(1000, 10000);
            gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        } while(gameDao.getGame(gameData) != null);

        gameDao.createGame(gameData);

        return new GameResult(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), null);
    }
}
