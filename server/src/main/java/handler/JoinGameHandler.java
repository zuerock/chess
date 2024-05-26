package handler;

import jsonConverter.JsonConverter;
import request.JoinGameRequest;
import result.Result;
import service.JoinGameService;
import spark.*;

public class JoinGameHandler implements Handler {
    private static JoinGameHandler instance;

    private JoinGameHandler() {}

    public static JoinGameHandler getInstance() {
        if (instance == null) {
            instance = new JoinGameHandler();
        }
        return instance;
    }

    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        JoinGameRequest request = converter.fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("authorization");
        request = new JoinGameRequest(request.playerColor(), request.gameID(), authToken);
        JoinGameService service = JoinGameService.getInstance();
        try {
            Result result = service.joinGame(request);
            if (result.message() != null) {
                if (result.message().equals("Error: bad request")) {
                    res.status(400);
                }
                else if (result.message().equals("Error: unauthorized")) {
                    res.status(401);
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
            return converter.toJson(new Result("Error: "+e.toString()));
        }
    }
}
