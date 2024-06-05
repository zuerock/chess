package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import result.ListGamesResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {

    public Object handle(Request request, Response response, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        String authToken = request.headers("authorization");
        GameService myGameService = new GameService();
        ListGamesResult myListGamesResult = myGameService.listGamesResult(authToken, authObj, gameObj);
        response.status(myListGamesResult.status);
        return myGson.toJson(myListGamesResult);
    }
}
