package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.interfaces.GameDao;
import dataAccess.sql.*;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import jsonConverter.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebSocket
public class WebSocketHandler {
    private final JsonConverter jsonConverter = JsonConverter.getInstance();
    private final Map<Integer, ConnectionManager> lobbies = new HashMap<>();
    private final ConnectionManager genericConnections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String json) throws IOException {
        UserGameCommand command = jsonConverter.fromJson(json, UserGameCommand.class);
        AuthData data = new AuthData(command.getAuthString(), null);
        try {
            data = SQLAuthDao.getInstance().getUser(data);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        if(data == null) {
            String output = "Unauthorized.";
            ErrorMessage message = new ErrorMessage(output);
            genericConnections.cancelSession(session, jsonConverter.toJson(message));
            return;
        }
        String username = data.username();

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayerCommand cmd = jsonConverter.fromJson(json, JoinPlayerCommand.class);
                String color = (cmd.getPlayerColor()==ChessGame.TeamColor.WHITE)?"white":"black";
                joinPlayer(username, cmd.getGameID(), color, session);
            }
            case JOIN_OBSERVER -> {
                JoinObserverCommand cmd = jsonConverter.fromJson(json, JoinObserverCommand.class);
                joinObserver(username, cmd.getGameID(), session);
            }
            case MAKE_MOVE -> {
                MakeMoveCommand cmd = jsonConverter.fromJson(json, MakeMoveCommand.class);
                makeMove(username, cmd.getGameID(), cmd.getMove());
            }
            case LEAVE -> {
                LeaveCommand cmd = jsonConverter.fromJson(json, LeaveCommand.class);
                leave(username, cmd.getGameID());
            }
            case RESIGN -> {
                ResignCommand cmd = jsonConverter.fromJson(json, ResignCommand.class);
                resign(username, cmd.getGameID());
            }
        }
    }

    private void joinPlayer(String username, Integer gameID, String color, Session session) throws IOException {
        ConnectionManager lobby = getLobby(gameID);
        lobby.add(username, session);

        try {
            GameDao dao = SQLGameDao.getInstance();
            GameData data = new GameData(gameID, null, null, null, null);
            data = dao.getGame(data);
            if(data == null) {
                String output = "No such game exists.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }
            if((color.equals("white") && !username.equals(data.whiteUsername()))
                    || (color.equals("black") && !username.equals(data.blackUsername()))) {
                String output = "HTTP join error.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }
            ChessGame game = data.game();



            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            lobby.send(username, jsonConverter.toJson(loadGameMessage));

            String output = username + " has joined the game as " + color + ".";
            NotificationMessage message = new NotificationMessage(output);
            lobby.broadcast(username, jsonConverter.toJson(message));
        } catch (DataAccessException e) {
            sendServerError(username, lobby, e);
        }
    }

    private void joinObserver(String username, Integer gameID, Session session) throws IOException {
        ConnectionManager lobby = getLobby(gameID);
        lobby.add(username, session);

        try {
            GameDao dao = SQLGameDao.getInstance();
            GameData data = new GameData(gameID, null, null, null, null);
            data = dao.getGame(data);
            if(data == null) {
                String output = "No such game exists.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }
            ChessGame game = data.game();

            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            lobby.send(username, jsonConverter.toJson(loadGameMessage));

            String output = username + " has joined the game as an observer.";
            NotificationMessage message = new NotificationMessage(output);
            lobby.broadcast(username, jsonConverter.toJson(message));
        } catch (DataAccessException e) {
            sendServerError(username, lobby, e);
        }
    }

    private void makeMove(String username, Integer gameID, ChessMove move) throws IOException {
        ConnectionManager lobby = getLobby(gameID);

        try {
            GameDao dao = SQLGameDao.getInstance();
            GameData data = new GameData(gameID,null,null,null,null);
            data = dao.getGame(data);
            ChessGame game = data.game();

            if((game.getTeamTurn() == ChessGame.TeamColor.WHITE && !username.equals(data.whiteUsername()))
                    || (game.getTeamTurn() == ChessGame.TeamColor.BLACK && !username.equals(data.blackUsername()))) {
                String output = "You can't make moves right now.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }
            if(game.isGameOver()) {
                String output = "Game has ended.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }


            try {
                game.makeMove(move);
            } catch (InvalidMoveException e) {
                String output = "Invalid Move.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }
            GameData updated = new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), game);
            dao.updateGame(updated);

            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            lobby.broadcast(null, jsonConverter.toJson(loadGameMessage));

            String output = username+" moved.";
            NotificationMessage message = new NotificationMessage(output);
            lobby.broadcast(username, jsonConverter.toJson(message));
        } catch (DataAccessException e) {
            sendServerError(username, lobby, e);
        }
    }

    private void leave(String username, Integer gameID) throws IOException {
        ConnectionManager lobby = getLobby(gameID);

        try {
            GameDao dao = SQLGameDao.getInstance();
            GameData data = new GameData(gameID,null,null,null,null);
            data = dao.getGame(data);
            GameData updated;
            String color;
            if (username.equals(data.whiteUsername())) {
                updated = new GameData(gameID, null, data.blackUsername(), data.gameName(), data.game());
                color = "white";
            }
            else if (username.equals(data.blackUsername())) {
                updated = new GameData(gameID, data.whiteUsername(), null, data.gameName(), data.game());
                color = "black";
            }
            else {
                updated = data;
                color = "observer";
            }
            dao.updateGame(updated);

            lobby.remove(username);
            String output = username+" ("+color+") has left the game.";
            NotificationMessage message = new NotificationMessage(output);
            lobby.broadcast(username, jsonConverter.toJson(message));
        } catch (DataAccessException e) {
            sendServerError(username, lobby, e);
        }
    }

    private void resign(String username, Integer gameID) throws IOException {
        ConnectionManager lobby = getLobby(gameID);

        try {
            GameDao dao = SQLGameDao.getInstance();
            GameData data = new GameData(gameID, null, null, null, null);
            data = dao.getGame(data);
            ChessGame game = data.game();

            if (!(username.equals(data.whiteUsername())||username.equals(data.blackUsername()))) {
                String output = "You can't resign.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }
            if(game.isGameOver()) {
                String output = "Game has ended.";
                ErrorMessage message = new ErrorMessage(output);
                lobby.send(username, jsonConverter.toJson(message));
                return;
            }

            game.setGameOver(true);
            GameData updated = new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), game);
            dao.updateGame(updated);

            String output = username+" has resigned.";
            NotificationMessage message = new NotificationMessage(output);
            lobby.broadcast(null, jsonConverter.toJson(message));
        } catch (DataAccessException e) {
            sendServerError(username, lobby, e);
        }
    }

    private void sendServerError(String username, ConnectionManager lobby, DataAccessException e) throws IOException {
        System.out.println(e.getMessage());
        String output = "A server error occurred.";
        ErrorMessage message = new ErrorMessage(output);
        lobby.send(username, jsonConverter.toJson(message));
    }

    private ConnectionManager getLobby(Integer gameID) {
        if (!lobbies.containsKey(gameID)) {
            lobbies.put(gameID,new ConnectionManager());
        }
        return lobbies.get(gameID);
    }
}