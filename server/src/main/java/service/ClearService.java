package service;

import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDao;
import dataAccess.interfaces.GameDao;
import dataAccess.interfaces.UserDao;
import dataAccess.memory.MemoryAuthDao;
import dataAccess.memory.MemoryGameDao;
import dataAccess.memory.MemoryUserDao;
import result.Result;

public class ClearService {
    private static ClearService instance;

    private ClearService() {}

    public static ClearService getInstance() {
        if (instance == null) {
            instance = new ClearService();
        }
        return instance;
    }

    public Result clear() throws DataAccessException {
        UserDao userDao = MemoryUserDao.getInstance();
        GameDao gameDao = MemoryGameDao.getInstance();
        AuthDao authDao = MemoryAuthDao.getInstance();
        authDao.clear();
        gameDao.clear();
        userDao.clear();
        return new Result(null);
    }
}
