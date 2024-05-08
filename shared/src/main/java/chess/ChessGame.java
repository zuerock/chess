package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard chessBoard;
    private TeamColor currentTurn;
    private boolean gameOver;

    public ChessGame() {
        this.chessBoard = new ChessBoard();
        this.chessBoard.resetBoard();
        this.currentTurn = TeamColor.WHITE;
        this.gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currPiece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> pieceMoves = new HashSet<>();
        Collection<ChessMove> valMoves = new HashSet<>();

        if (currPiece == null || currPiece.pieceMoves(chessBoard, startPosition).isEmpty()) {
            return null;
        } else {
            pieceMoves = currPiece.pieceMoves(chessBoard, startPosition);
        }

        for (ChessMove move : pieceMoves) {
            // Will this move put us into check?
            ChessPosition endPos = move.getEndPosition();
            ChessPosition startPos = move.getStartPosition();
        }

        return valMoves;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(chessBoard, chessGame.chessBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "chessBoard=" + chessBoard +
                ", currentTurn=" + currentTurn +
                '}';
    }
}