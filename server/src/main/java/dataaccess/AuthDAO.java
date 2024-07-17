package dataaccess;

import java.sql.SQLException;

public interface AuthDAO {
    boolean isEmpty() throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    String getAuth(String authToken) throws DataAccessException;

    String getUsername(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void removeAllAuthTokens() throws DataAccessException;
}
