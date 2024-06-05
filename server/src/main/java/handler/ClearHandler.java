package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.DBService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public Object handle(Request request, Response response, UserDAO userObj, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        DBService myDBService = new DBService();
        return myGson.toJson(myDBService.clearResult(userObj, authObj, gameObj));
    }
}
