package handler;

import jsonConverter.JsonConverter;
import request.LogoutRequest;
import result.Result;
import service.LogoutService;
import spark.*;

public class LogoutHandler implements Handler {
    private static LogoutHandler instance;

    private LogoutHandler() {}

    public static LogoutHandler getInstance() {
        if (instance == null) {
            instance = new LogoutHandler();
        }
        return instance;
    }

    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        LogoutService service = LogoutService.getInstance();
        String authToken = req.headers("authorization");
        LogoutRequest request = new LogoutRequest(authToken);
        try {
            Result result = service.logout(request);
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
            return converter.toJson(new Result("Error: "+e.toString()));
        }
    }
}
