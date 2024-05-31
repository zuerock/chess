package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import request.RegisterRequest;
import result.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handle (Request request, Response response, UserDAO userObj, AuthDAO authObj){
        Gson myGson = new Gson();
        RegisterRequest myRequest = myGson.fromJson(request.body(), RegisterRequest.class);
        UserService myUserService = new UserService();
        RegisterResult myRegResponse = myUserService.regResult(myRequest, userObj, authObj);
        response.status(myRegResponse.status);
        return myGson.toJson(myRegResponse);
    }
}
