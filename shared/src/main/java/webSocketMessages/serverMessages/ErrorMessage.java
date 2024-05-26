package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
