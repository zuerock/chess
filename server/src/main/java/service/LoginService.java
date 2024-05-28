package service;

import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.UserDao;
import dataAccess.memory.MemoryAuthDao;
import dataAccess.memory.MemoryUserDao;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.LoginRequest;
import result.AuthResult;

import java.util.UUID;

public class LoginService {
    private static LoginService instance;

    private LoginService() {}

    public static LoginService getInstance() {
        if (instance == null) {
            instance = new LoginService();
        }
        return instance;
    }

    public AuthResult login(LoginRequest request) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDao userDao = MemoryUserDao.getInstance();
        String username = request.username();
        String password = request.password();
        UserData userData = new UserData(username, password, null);

        if(userDao.getUser(userData) == null || !encoder.matches(password, userDao.getUser(userData).password())) {
            return new AuthResult(null, null, "Error: unauthorized");
        }

        AuthDao authDao = MemoryAuthDao.getInstance();
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);

        authDao.createAuth(authData);

        return new AuthResult(username, authToken, null);
    }
}
