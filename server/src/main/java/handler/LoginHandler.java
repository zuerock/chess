package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import request.LoginRequest;
import result.LoginResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {

    public Object handle(Request request, Response response, UserDAO userObj, AuthDAO authObj) {
        Gson myGson = new Gson();
        LoginRequest myRequest = myGson.fromJson(request.body(), LoginRequest.class);
        UserService myUserService = new UserService();
        LoginResult myLoginResult = myUserService.loginResult(myRequest, userObj, authObj);
        response.status(myLoginResult.status);
        return myGson.toJson(myLoginResult);
    }
}
