package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.JoinGameResult;

public class GameService {
    public ListGamesResult listGamesResult(String authToken, AuthDAO authObj, GameDAO gameObj) {
        boolean authenticated = false;
        // Check for authentication
        for (int i = 0; i < authObj.authList.size(); i = i + 1) {
            if (authToken.equals(authObj.authList.get(i).authToken())) {
                authenticated = true;
                break;
            }
        }
        if (authenticated) {
            return new ListGamesResult(gameObj.gameList, "", 200);
        } else {
            return new ListGamesResult(null, "ERROR - Unauthorized", 401);
        }
    }

    public CreateGameResult createGameResult(CreateGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj) {
        boolean authenticated = false;

        if (req.getGameName() == null) {
            return new CreateGameResult(null, "ERROR - Bad Request", 400);
        }
        // Check for authentication
        for (int i = 0; i < authObj.authList.size(); i = i + 1) {
            if (authObj.authList.get(i).authToken().equals(authToken)) {
                authenticated = true;
                break;
            }
        }

        if (authenticated) {
            int newGameID = gameObj.currentID;
            gameObj.currentID = gameObj.currentID + 1;
            GameData gameDataToAdd = new GameData(newGameID, null, null, req.getGameName(), new ChessGame());
            gameObj.gameList.add(gameDataToAdd);
            return new CreateGameResult(newGameID, null, 200);
        } else {
            return new CreateGameResult(null, "ERROR - Unauthorized", 401);
        }
    }

    public JoinGameResult joinGameResult(JoinGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj) {
        boolean authenticated = false;
        int userNumber = 0;

        if (req.gameID == 0) {
            return new JoinGameResult("ERROR - Bad Request", 400);
        } else {
            // Check for authentication
            for (int i = 0; i < authObj.authList.size(); i = i + 1) {
                if (authObj.authList.get(i).authToken().equals(authToken)) {
                    authenticated = true;
                    userNumber = i;
                    break;
                }
            }
        }

        if (authenticated) {
            for (int i = 0; i < gameObj.gameList.size(); i = i + 1) {
                // Does the game exist?
                if (gameObj.gameList.get(i).gameID() == req.getGameID()) {
                    if (req.getPlayerColor() == null) {
                        // Joins as observer
                        return new JoinGameResult("ERROR - Already taken", 400);
                    } else {
                        if (req.getPlayerColor() != null && req.getPlayerColor().equals("WHITE") && gameObj.gameList.get(i).whiteUsername() == null) {
                            String newWhite = authObj.authList.get(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), newWhite, gameObj.gameList.get(i).blackUsername(), gameObj.gameList.get(i).gameName(), gameObj.gameList.get(i).game());
                            gameObj.gameList.set(i, gameToUpdate);
                            return new JoinGameResult("", 200);
                        } else if (req.getPlayerColor() != null && req.getPlayerColor().equals("BLACK") && gameObj.gameList.get(i).blackUsername() == null) {
                            String newBlack = authObj.authList.get(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), gameObj.gameList.get(i).whiteUsername(), newBlack, gameObj.gameList.get(i).gameName(), gameObj.gameList.get(i).game());
                            gameObj.gameList.set(i, gameToUpdate);
                            return new JoinGameResult("", 200);
                        } else {
                            return new JoinGameResult("ERROR - Already taken", 403);
                        }
                    }
                }
            }
        }
        return new JoinGameResult("ERROR - Unauthorized", 401);
    }
}
