package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private Integer gameID;

    public ResignCommand(String auth, Integer gameID) {
        this.commandType = CommandType.RESIGN;
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
