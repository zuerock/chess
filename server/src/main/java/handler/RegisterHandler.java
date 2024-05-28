package handler;

import jsonConverter.JsonConverter;
import request.RegisterRequest;
import service.RegisterService;
import result.AuthResult;
import spark.*;

public class RegisterHandler implements Handler {
    private static RegisterHandler instance;

    private RegisterHandler() {}

    public static RegisterHandler getInstance() {
        if (instance == null) {
            instance = new RegisterHandler();
        }
        return instance;
    }

    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        RegisterRequest request = converter.fromJson(req.body(), RegisterRequest.class);
        RegisterService service = RegisterService.getInstance();
        try {
            AuthResult result = service.register(request);
            if (result.message() != null) {
                if (result.message().equals("Error: bad request")) {
                    res.status(400);
                } else if (result.message().equals("Error: already taken")) {
                    res.status(403);
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
