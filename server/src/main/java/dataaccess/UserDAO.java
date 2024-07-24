package dataaccess;

import model.GameData;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUser(UserData user) throws DataAccessException;

    void removeAllUsers() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}
