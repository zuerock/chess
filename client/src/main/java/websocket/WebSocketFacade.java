package websocket;

import chess.ChessGame;
import chess.ChessMove;
import jsonConverter.JsonConverter;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.*;
import java.net.*;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    CommandHandler commandHandler;
    JsonConverter jsonConverter = JsonConverter.getInstance();
    private String authToken;
    private String username;

    public WebSocketFacade(int port, CommandHandler commandHandler) {
        try {
            String url = "ws://localhost:"+port;
            URI socketURI = new URI(url + "/connect");
            this.commandHandler = commandHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    ServerMessage notification = jsonConverter.fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case NOTIFICATION -> notification = jsonConverter.fromJson(message, NotificationMessage.class);
                        case LOAD_GAME -> notification = jsonConverter.fromJson(message, LoadGameMessage.class);
                        case ERROR -> notification = jsonConverter.fromJson(message, ErrorMessage.class);
                    }
                    commandHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String auth, Integer gameID, ChessGame.TeamColor color) {
        try {
            UserGameCommand command = new JoinPlayerCommand(auth, gameID, color);
            this.session.getBasicRemote().sendText(jsonConverter.toJson(command));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void joinObserver(String auth, Integer gameID) {
        try {
            UserGameCommand command = new JoinObserverCommand(auth, gameID);
            this.session.getBasicRemote().sendText(jsonConverter.toJson(command));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void makeMove(String auth, Integer gameID, ChessMove move) {
        try {
            UserGameCommand command = new MakeMoveCommand(auth, gameID, move);
            this.session.getBasicRemote().sendText(jsonConverter.toJson(command));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void resign(String auth, Integer gameID) {
        try {
            UserGameCommand command = new ResignCommand(auth, gameID);
            this.session.getBasicRemote().sendText(jsonConverter.toJson(command));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void leave(String auth, Integer gameID) {
        try {
            UserGameCommand command = new LeaveCommand(auth, gameID);
            this.session.getBasicRemote().sendText(jsonConverter.toJson(command));
            this.session.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}