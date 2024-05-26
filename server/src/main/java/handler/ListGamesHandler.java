package handler;

import jsonConverter.JsonConverter;
import request.ListGamesRequest;
import result.GameListResult;
import service.ListGamesService;
import spark.*;

public class ListGamesHandler implements Handler {
    private static ListGamesHandler instance;

    private ListGamesHandler() {}

    public static ListGamesHandler getInstance() {
        if (instance == null) {
            instance = new ListGamesHandler();
        }
        return instance;
    }

    @Override
    public Object handle(Request req, Response res) {
        JsonConverter converter = JsonConverter.getInstance();
        String authToken = req.headers("authorization");
        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesService service = ListGamesService.getInstance();
        try {
            GameListResult result = service.listGames(request);
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
            return converter.toJson(new GameListResult(null, "Error: "+e.toString()));
        }
    }
}
