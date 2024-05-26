package dataAccess.sql;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.GameDao;
import dataAccess.interfaces.UserDao;
import model.GameData;
import jsonConverter.JsonConverter;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class SQLGameDao extends ConnectionManager implements GameDao {
    private JsonConverter converter = JsonConverter.getInstance();
    private static SQLGameDao instance;
    private Set<GameData> game = new HashSet<>();
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256),
              `game` varchar(2048),
              PRIMARY KEY (`gameID`)
            )
            """
    };

    private SQLGameDao() throws DataAccessException {
        UserDao dependency = SQLUserDao.getInstance();
        configureDatabase(createStatements);
    }

    public static SQLGameDao getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLGameDao();
        }
        return instance;
    }

    @Override
    public void createGame(GameData data) {
        String sql = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1,data.gameID());
                preparedStatement.setString(2,data.whiteUsername());
                preparedStatement.setString(3,data.blackUsername());
                preparedStatement.setString(4,data.gameName());
                preparedStatement.setString(5,converter.toJson(data.game()));
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Added game with ID: "+data.gameID());
                }
                else {
                    System.out.println("Failed to add game with ID: "+data.gameID());
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public GameData getGame(GameData data) {
        String sql = "SELECT * FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1,data.gameID());

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = converter.fromJson(rs.getString("game"), ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                }
                else {
                    return null;
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public Set<GameData> listGames() {
        Set<GameData> games = new HashSet<>();
        String sql = "SELECT * FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = converter.fromJson(rs.getString("game"), ChessGame.class);
                    games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                }
                return games;
            }
        } catch(Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public void updateGame(GameData data) {
        String sql = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,data.whiteUsername());
                preparedStatement.setString(2,data.blackUsername());
                preparedStatement.setString(3,data.gameName());
                preparedStatement.setString(4,converter.toJson(data.game()));
                preparedStatement.setInt(5,data.gameID());
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Updated game with ID: "+data.gameID());
                }
                else {
                    System.out.println("Failed to update game with ID: "+data.gameID());
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Cleared game table");
                }
                else {
                    System.out.println("Failed to clear game table");
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
