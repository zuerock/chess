package request;

public class JoinGameRequest {
    public String playerColor;
    public int gameID;

    public JoinGameRequest(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
        if(playerColor == null) {
            this.gameID = 0;
        }
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
