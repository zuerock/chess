package handler;

import jsonConverter.JsonConverter;
import request.CreateGameRequest;
import result.GameResult;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler implements Handler {
    private static CreateGameHandler instance;

    private CreateGameHandler() {}

    public static CreateGameHandler getInstance() {
        if (instance == null) {
            instance = new CreateGameHandler();
        }
        return instance;
    }

    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        CreateGameRequest request = converter.fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("authorization");
        request = new CreateGameRequest(request.gameName(), authToken);
        CreateGameService service = CreateGameService.getInstance();
        try {
            GameResult result = service.createGame(request);
            if (result.message() != null) {
                if (result.message().equals("Error: bad request")) {
                    res.status(400);
                }
                else if (result.message().equals("Error: unauthorized")) {
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
            return converter.toJson(new GameResult(null, null, null, null,
                    "Error: "+e.toString()));
        }
    }
}
