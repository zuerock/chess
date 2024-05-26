package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.*;
import model.AuthData;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class SQLAuthDao extends ConnectionManager implements AuthDao {
    private static SQLAuthDao instance;
    private Set<AuthData> auth = new HashSet<>();
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `id` int NOT NULL AUTO_INCREMENT,
              `authtoken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            )
            """
    };

    private SQLAuthDao() throws DataAccessException {
        UserDao dependency = SQLUserDao.getInstance();
        configureDatabase(createStatements);
    }

    public static SQLAuthDao getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLAuthDao();
        }
        return instance;
    }

    @Override
    public void createAuth(AuthData data) {
        String sql = "INSERT INTO auth (authtoken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,data.authtoken());
                preparedStatement.setString(2,data.username());
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Added authtoken for user: "+data.username());
                }
                else {
                    System.out.println("Failed to add authtoken for user: "+data.username());
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public AuthData getUser(AuthData data) {
        String sql = "SELECT * FROM auth WHERE authtoken=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,data.authtoken());

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String authtoken = rs.getString("authtoken");
                    String username = rs.getString("username");
                    return new AuthData(authtoken, username);
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
    public void deleteAuth(AuthData data) {
        String sql = "DELETE FROM auth WHERE authtoken=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,data.authtoken());
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Deleted authtoken for user: "+data.username());
                }
                else {
                    System.out.println("Failed to delete authtoken for user: "+data.username());
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Cleared authtoken table");
                }
                else {
                    System.out.println("Failed to clear authtoken table");
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
