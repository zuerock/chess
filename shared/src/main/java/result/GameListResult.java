package result;

import model.GameData;

import java.util.Set;

public record GameListResult(Set<GameData> games, String message) {}
