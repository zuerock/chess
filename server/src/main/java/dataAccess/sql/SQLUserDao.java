package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.UserDao;
import model.UserData;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class SQLUserDao extends ConnectionManager implements UserDao {
    private static SQLUserDao instance;
    private Set<UserData> user = new HashSet<>();
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    private SQLUserDao() throws DataAccessException {
        configureDatabase(createStatements);
    }

    public static SQLUserDao getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLUserDao();
        }
        return instance;
    }

    @Override
    public void createUser(UserData data) {
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,data.username());
                preparedStatement.setString(2,data.password());
                preparedStatement.setString(3,data.email());
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Added user with username: "+data.username());
                }
                else {
                    System.out.println("Failed to add user with username: "+data.username());
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public UserData getUser(UserData data) {
        String sql = "SELECT * FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1,data.username());

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
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
    public void clear() {
        String sql = "DELETE FROM user";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sql)) {
                if (preparedStatement.executeUpdate() >= 1) {
                    System.out.println("Cleared user table");
                }
                else {
                    System.out.println("Failed to clear user table");
                }
            }
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
