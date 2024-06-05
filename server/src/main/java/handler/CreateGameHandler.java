package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {

    public Object handle(Request request, Response response, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        CreateGameRequest myRequest = myGson.fromJson(request.body(), CreateGameRequest.class);
        String authToken = request.headers("authorization");
        GameService myGameService = new GameService();
        CreateGameResult myCreateGameResult = myGameService.createGameResult(myRequest, authToken, authObj, gameObj);
        response.status(myCreateGameResult.status);
        return myGson.toJson(myCreateGameResult);
    }
}
