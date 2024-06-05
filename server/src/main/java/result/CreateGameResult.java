package result;

public class CreateGameResult {
    public Integer gameID;
    public String message;
    public int status;

    public CreateGameResult(Integer gameID, String message, int status) {
        this.gameID = gameID;
        this.message = message;
        this.status = status;
    }
}
