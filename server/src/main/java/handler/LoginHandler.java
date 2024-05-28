package handler;

import jsonConverter.JsonConverter;
import request.LoginRequest;
import result.AuthResult;
import service.LoginService;
import spark.*;

public class LoginHandler implements Handler {
    private static LoginHandler instance;

    private LoginHandler() {}

    public static LoginHandler getInstance() {
        if (instance == null) {
            instance = new LoginHandler();
        }
        return instance;
    }

    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        LoginRequest request = converter.fromJson(req.body(), LoginRequest.class);
        LoginService service = LoginService.getInstance();
        try {
            AuthResult result = service.login(request);
            if (result.message() != null) {
                if (result.message().equals("Error: unauthorized")) {
                    res.status(401);
                } else {
                    res.status(500);
                }
            } else {
                res.status(200);
            }
            return converter.toJson(result);
        }
        catch (Exception e) {
            res.status(500);
            return converter.toJson(new AuthResult(null, null, "Error: "+e.toString()));
        }
    }
}
