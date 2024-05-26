package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private Integer gameID;

    public LeaveCommand(String auth, Integer gameID) {
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
        this.authToken = auth;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
