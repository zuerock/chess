package dataaccess;

import model.GameData;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public List<GameData> gameList = new ArrayList<>();
    public int currentID = 1;

    public void createGame(GameData game){};

    public int getCurrentID(){return currentID;};

    public void setCurrentID(int i){};

    public void removeGame(int index){};

    public GameData getGame(int index){return gameList.get(index);};

    public void setGame(int index, GameData game){};

    public int getSize(){return gameList.size();};

    public void clearGameList(){
        gameList.clear();
    }

    public List<GameData> returnGameList(){return gameList;};
}
