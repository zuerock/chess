package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import result.ClearResult;

public class DBService {
    public ClearResult clearResult(UserDAO userObj, AuthDAO authObj, GameDAO gameObj){
        userObj.clearUserList();
        authObj.clearAuthList();
        gameObj.clearGameList();
        ClearResult clearResult = new ClearResult(null);
        return clearResult;
    }
}
