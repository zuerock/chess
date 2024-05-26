package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private Integer gameID;
    private ChessMove move;

    public MakeMoveCommand(String auth, Integer gameID, ChessMove move) {
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
        this.authToken = auth;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
