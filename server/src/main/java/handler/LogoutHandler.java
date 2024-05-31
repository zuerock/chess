package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import result.LogoutResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public Object handle(Request request, Response response, AuthDAO authObj){
        Gson myGson = new Gson();
        String authToken = request.headers("authorization");
        UserService myUserService = new UserService();
        LogoutResult myLogoutResult = myUserService.logoutResult(authToken, authObj);
        response.status(myLogoutResult.status);
        return myGson.toJson(myLogoutResult);
    }
}
