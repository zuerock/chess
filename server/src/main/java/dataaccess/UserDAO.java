package dataaccess;

import request.RegisterRequest;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public void createUser(RegisterRequest register) throws DataAccessException;
    public void validateUserPassword(String username, String password) throws DataAccessException;
    public void configureDatabase() throws DataAccessException;
}
