package result;

import model.GameData;

import java.util.List;

public class ListGamesResult {
    public List<GameData> games;
    public String message;
    public int status;

    public ListGamesResult(List<GameData> games, String message, int status) {
        this.games = games;
        this.message = message;
        this.status = status;
    }
}
