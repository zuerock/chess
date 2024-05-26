package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private Integer gameID;

    public JoinObserverCommand(String auth, Integer gameID) {
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
        this.authToken = auth;
    }

    public Integer getGameID() {
        return gameID;
    }
}
