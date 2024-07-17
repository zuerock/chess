package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameDAO {
    boolean isEmpty() throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    Integer createGame(String gameName, Integer gameID) throws DataAccessException;

    void saveGame(int gameID, GameData game) throws DataAccessException;

    void removeAllGames() throws DataAccessException;


}
