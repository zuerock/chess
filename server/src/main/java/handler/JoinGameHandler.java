package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    public Object handle(Request request, Response response, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        JoinGameRequest myRequest = myGson.fromJson(request.body(), JoinGameRequest.class);
        String authToken = request.headers("authorization");
        GameService myGameService = new GameService();
        JoinGameResult myJoinGameResult = myGameService.joinGameResult(myRequest, authToken, authObj, gameObj);
        response.status(myJoinGameResult.status);
        return myGson.toJson(myJoinGameResult);
    }
}
