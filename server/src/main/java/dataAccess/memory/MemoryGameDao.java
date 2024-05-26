package dataAccess.memory;

import dataAccess.interfaces.GameDao;
import model.GameData;

import java.util.HashSet;
import java.util.Set;

public class MemoryGameDao implements GameDao {
    private static MemoryGameDao instance;
    private Set<GameData> game = new HashSet<>();

    private MemoryGameDao() {}

    public static MemoryGameDao getInstance() {
        if (instance == null) {
            instance = new MemoryGameDao();
        }
        return instance;
    }

    @Override
    public void createGame(GameData data) {
        game.add(data);
    }

    @Override
    public GameData getGame(GameData data) {
        for (GameData g : game) {
            if (g.gameID().equals(data.gameID())) {
                return g;
            }
        }
        return null;
    }

    @Override
    public Set<GameData> listGames() {
        Set<GameData> games = new HashSet<>();
        for (GameData g : game) {
            games.add(new GameData(g.gameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), null));
        }
        return games;
    }

    @Override
    public void updateGame(GameData data) {
        for (GameData g : game) {
            if (g.gameID().equals(data.gameID())) {
                game.remove(g);
                game.add(data);
                return;
            }
        }
    }

    @Override
    public void clear() {
        game = new HashSet<>();
    }
}
