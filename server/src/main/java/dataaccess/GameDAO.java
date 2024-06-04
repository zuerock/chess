package dataaccess;

import model.GameData;
import java.util.ArrayList;
import java.util.List;

public abstract class GameDAO {

    public List<GameData> gameList = new ArrayList<>();
    public int currentID = 1;

    public abstract void createGame(GameData game);

    public abstract int getCurrentID();

    public abstract void setCurrentID(int i);

    public abstract void removeGame(int index);

    public abstract GameData getGame(int index);

    public abstract void setGame(int index, GameData game);

    public abstract int getSize();

    public void clearGameList(){
        gameList.clear();
    }

    public abstract List<GameData> returnGameList();
}
