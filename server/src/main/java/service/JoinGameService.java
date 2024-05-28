package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.AuthData;
import model.GameData;
import request.JoinGameRequest;
import result.Result;

public class JoinGameService {
    private static JoinGameService instance;

    private JoinGameService() {}

    public static JoinGameService getInstance() {
        if (instance == null) {
            instance = new JoinGameService();
        }
        return instance;
    }

    public Result joinGame(JoinGameRequest request) throws DataAccessException {
        if(request.gameID() == null) {
            return new Result("Error: bad request");
        }
        String playerColor = request.playerColor();
        if (playerColor != null) {
            if (!(playerColor.equals("BLACK") || playerColor.equals("WHITE") || playerColor.equals(""))) {
                return new Result("Error: bad request");
            }
        }

        AuthDao authDao = SQLAuthDao.getInstance();
        String authToken = request.authToken();
        AuthData authData = new AuthData(authToken, null);
        authData = authDao.getUser(authData);
        if(authData == null) {
            return new Result("Error: unauthorized");
        }

        GameDao gameDao = SQLGameDao.getInstance();
        GameData gameData = new GameData(request.gameID(), null, null, null, null);
        gameData = gameDao.getGame(gameData);
        if (gameData == null) {
            return new Result("Error: bad request");
        }

        if (!(playerColor == null || playerColor.equals(""))) {
            if (playerColor.equals("WHITE") && gameData.whiteUsername() == null) {
                gameData = new GameData(gameData.gameID(), authData.username(),
                        gameData.blackUsername(), gameData.gameName(), new ChessGame());
                gameDao.updateGame(gameData);
            } else if (playerColor.equals("BLACK") && gameData.blackUsername() == null) {
                gameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                        authData.username(), gameData.gameName(), new ChessGame());
                gameDao.updateGame(gameData);
            } else {
                return new Result("Error: already taken");
            }
        }

        return new Result(null);
    }
}
