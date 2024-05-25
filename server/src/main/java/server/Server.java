package server;

import spark.*;
import handler.*;
import websocket.WebSocketHandler;

public class Server {

    public static void main(String[] args) {
        var port = 8080;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        Server server = new Server();
        server.run(port);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        WebSocketHandler webSocketHandler = new WebSocketHandler();

        Spark.webSocket("/connect", webSocketHandler);

        createEndpoints();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createEndpoints() {
        Spark.delete("/db", (req, res)->                        // clear
                (ClearHandler.getInstance()).handle(req, res));      //

        Spark.post("/user", (req, res)->                        // register
                (RegisterHandler.getInstance()).handle(req, res));   //

        Spark.post("/session", (req, res)->                     // login
                (LoginHandler.getInstance()).handle(req, res));      //

        Spark.delete("/session", (req, res)->                   // logout
                (LogoutHandler.getInstance()).handle(req, res));     //

        Spark.get("/game", (req, res)->                         // list games
                (ListGamesHandler.getInstance()).handle(req, res));  //

        Spark.post("/game", (req, res)->                        // create game
                (CreateGameHandler.getInstance()).handle(req, res)); //

        Spark.put("/game", (req, res)->                         // join game
                (JoinGameHandler.getInstance()).handle(req, res));   //
    }
}
