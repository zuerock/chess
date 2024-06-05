package dataaccess;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("TRUNCATE auth")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void createAuth(String token, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void authExists(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT authToken FROM auth WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    if(!rs.next()) {
                        throw new DataAccessException("Error: unauthorized");
                    }
                }
            }
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getUserFromAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try(var rs = preparedStatement.executeQuery()){
                    if(!rs.next()){
                        throw new DataAccessException("Error: unauthorized");
                    }
                    String username = rs.getString("username");
                    return username;
                }
            }
        }
        catch (DataAccessException e){
            throw e;
        }
        catch (Exception e){
            throw new DataAccessException("Error: bad request");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) 
            """
    };

    @Override
    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
