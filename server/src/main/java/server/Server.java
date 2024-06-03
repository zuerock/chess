package server;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.*;
import spark.*;

public class Server {

    UserDAO userObj = new UserDAO();
    AuthDAO authObj = new AuthDAO();
    GameDAO gameObj = new GameDAO();

    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.run(Integer.parseInt(args[0]));
    }

    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", ((request, result) -> new ClearHandler().handle(request, result, userObj, authObj, gameObj)));
        Spark.post("/user", ((request, result) -> new RegisterHandler().handle(request, result, userObj, authObj)));
        Spark.post("/session", ((request, result) -> new LoginHandler().handle(request, result, userObj, authObj)));
        Spark.delete("/session", ((request, result) -> new LogoutHandler().handle(request, result, authObj)));
        Spark.get("/game", ((request, result) -> new ListGamesHandler().handle(request, result, authObj, gameObj)));
        Spark.post("/game", ((request, result) -> new CreateGameHandler().handle(request, result, authObj, gameObj)));
        Spark.put("/game", ((request, result) -> new JoinGameHandler().handle(request, result, authObj, gameObj)));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
