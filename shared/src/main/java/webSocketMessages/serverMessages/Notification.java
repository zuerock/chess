package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{
    String message;

    public Notification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
