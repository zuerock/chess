package websocket;

import webSocketMessages.serverMessages.*;

public interface CommandHandler {
    void notify(ServerMessage notification);
}