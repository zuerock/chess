package dataAccess.interfaces;

import model.GameData;

import java.util.Set;

public interface GameDao {
    public void createGame(GameData data);
    public GameData getGame(GameData data);
    public Set<GameData> listGames();
    public void updateGame(GameData data);
    public void clear();
}
