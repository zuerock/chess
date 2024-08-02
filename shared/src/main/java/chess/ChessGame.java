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
public class ChessGame implements Cloneable {

    private ChessBoard chessBoard;
    private TeamColor currentTurn;

    public ChessGame() {
        this.chessBoard = new ChessBoard();
        this.chessBoard.resetBoard();
        this.currentTurn = TeamColor.WHITE;

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
        ChessPiece currentPiece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> pieceMoves = new HashSet<>();
        Collection<ChessMove> validMoves = new HashSet<>();

        if (currentPiece == null || currentPiece.pieceMoves(chessBoard, startPosition).isEmpty()) {
            validMoves.add(null);
            return validMoves;
        } else {
            pieceMoves = currentPiece.pieceMoves(chessBoard, startPosition);
        }

        for (ChessMove move : pieceMoves) {
            // Will this move put us into check?
            ChessPosition endPosition = move.getEndPosition();
            startPosition = move.getStartPosition();

            if (this.chessBoard.getPiece(endPosition) == null || (this.chessBoard.getPiece(endPosition) != null && this.chessBoard.getPiece(endPosition).getTeamColor() != currentPiece.getTeamColor())) {
                // Create alternate universe game to see if move will put us into check
                ChessGame altGame = this.clone();
                altGame.chessBoard.addPiece(endPosition, currentPiece);
                altGame.chessBoard.addPiece(startPosition, null);

                // If move puts us into check, remove it from the moves
                if (!altGame.isInCheck(currentPiece.getTeamColor())) {
                    validMoves.add(move);
                }
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promoPiece = move.getPromotionPiece();

        // Get current piece
        ChessPiece currentPiece = this.chessBoard.getPiece(startPosition);
        if(currentPiece == null){
            throw new InvalidMoveException("ERROR: No Piece found for " + startPosition);
        }
        if (isInCheckmate(currentTurn)) {
            throw new InvalidMoveException("ERROR: IN CHECKMATE");
        }

        // Check that it's the piece's turn
        if (currentPiece.getTeamColor() == getTeamTurn()) {
            Collection<ChessMove> validMoves = validMoves(startPosition);
            // Is the move valid?
            if (validMoves == null) {
                throw new InvalidMoveException("ERROR: NO VALID MOVES EXIST");
            } else if (!(validMoves.contains(move))) {
                throw new InvalidMoveException("ERROR: INVALID MOVE");
            }
            // Are we in check?
            if (isInCheck(getTeamTurn())) {
                // Will this move take us out of check?
                if (this.chessBoard.getPiece(endPosition) == null || (this.chessBoard.getPiece(endPosition) != null && this.chessBoard.getPiece(endPosition).getTeamColor() != currentTurn)) {
                    // Create alternate universe game to see if move will take us out of check
                    ChessGame altGame = this.clone();
                    altGame.chessBoard.addPiece(endPosition, currentPiece);
                    altGame.chessBoard.addPiece(startPosition, null);
                    if (altGame.isInCheck(getTeamTurn())) {
                        throw new InvalidMoveException("ERROR: STILL IN CHECK");
                    } else {
                        isPromotingPawn(currentPiece, promoPiece, startPosition, endPosition);
                    }
                } else {
                    throw new InvalidMoveException("ERROR: CURRENTLY IN CHECK");
                }
            } else {
                // Alternate board to see if the move will put us in check
                ChessGame altGame = this.clone();
                altGame.chessBoard.addPiece(endPosition, currentPiece);
                altGame.chessBoard.addPiece(startPosition, null);
                if (altGame.isInCheck(getTeamTurn())) {
                    throw new InvalidMoveException("ERROR: MOVE WILL PUT YOU INTO CHECK");
                } else {
                    isPromotingPawn(currentPiece, promoPiece, startPosition, endPosition);
                }
            }

        } else {
            throw new InvalidMoveException("ERROR: WRONG TURN");
        }
        // Change turn to the other team
        currentTurn = currentPiece.getTeamColor() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    // Promoting Pawn Helper
    public void isPromotingPawn(ChessPiece currentPiece, ChessPiece.PieceType promoPiece, ChessPosition startPosition, ChessPosition endPosition){
        if (promoPiece != null) {
            ChessPiece promotedPiece = new ChessPiece(currentTurn, promoPiece);
            this.chessBoard.addPiece(endPosition, promotedPiece);
            this.chessBoard.addPiece(startPosition, null);
        } else {
            this.chessBoard.addPiece(endPosition, currentPiece);
            this.chessBoard.addPiece(startPosition, null);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor attackingTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        boolean inCheck = false;

        outermostLoop:
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition attackingPosition = new ChessPosition(i, j);
                ChessPiece attackingPiece = this.chessBoard.getPiece(attackingPosition);

                if (attackingPiece != null && attackingPiece.getTeamColor() == attackingTeam) {
                    Collection<ChessMove> attackingMoves = attackingPiece.pieceMoves(this.chessBoard, attackingPosition);

                    for (ChessMove move : attackingMoves) {
                        // Is the attacking piece going to hit the king?
                        if (this.chessBoard.getPiece(move.getEndPosition()) != null && this.chessBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && this.chessBoard.getPiece(move.getEndPosition()).getTeamColor() != attackingTeam) {
                            inCheck = true;
                            break outermostLoop;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Let's assume we're not in checkmate and then prove that we are
        boolean inCheckmate = false;

        if (!isInCheck(currentTurn)) return false;

        // Try to move all friendly team pieces. If any of them take us out of check, keep inCheckmate as false. Otherwise, make it true
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition friendlyPosition = new ChessPosition(i, j);
                ChessPiece friendlyPiece = this.chessBoard.getPiece(friendlyPosition);

                if (friendlyPiece == null || friendlyPiece.getTeamColor() != teamColor) continue;

                // Try to move.
                Collection<ChessMove> friendlyMoves = friendlyPiece.pieceMoves(this.chessBoard, friendlyPosition);

                for (ChessMove move : friendlyMoves) {
                    // Will this move take us out of check?
                    ChessPosition endPosition = move.getEndPosition();
                    ChessPosition startPosition = move.getStartPosition();

                    // Create alternate universe game to see if move will take us out of check
                    ChessGame altGame = this.clone();
                    altGame.chessBoard.addPiece(endPosition, friendlyPiece);
                    altGame.chessBoard.addPiece(startPosition, null);

                    // If all moves keep us in check, we're in checkmate
                    if (!altGame.isInCheck(teamColor)) {
                        return false;
                    } else {
                        inCheckmate = true;
                    }
                }
            }
        }

        return inCheckmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if(isInCheck(teamColor)) return false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition friendlyPosition = new ChessPosition(i, j);
                ChessPiece friendlyPiece = this.chessBoard.getPiece(friendlyPosition);

                if (friendlyPiece != null && friendlyPiece.getTeamColor() == teamColor) {
                    // Can this piece move at all? If it can, we are not in a stalemate - break to outermostLoop
                    Collection<ChessMove> friendlyMoves = friendlyPiece.pieceMoves(this.chessBoard, friendlyPosition);
                    for (ChessMove move : friendlyMoves) {
                        // Will this move put us into check?
                        ChessPosition endPos = move.getEndPosition();
                        ChessPosition startPos = move.getStartPosition();

                        if (this.chessBoard.getPiece(endPos) == null || (this.chessBoard.getPiece(endPos) != null && this.chessBoard.getPiece(endPos).getTeamColor() != currentTurn)) {
                            // Create alternate universe game to see if move will put us into check
                            ChessGame altGame = this.clone();
                            altGame.chessBoard.addPiece(endPos, friendlyPiece);
                            altGame.chessBoard.addPiece(startPos, null);

                            // If all moves put us in check, we're in stalemate
                            if (!altGame.isInCheck(teamColor)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
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

    @Override
    public ChessGame clone() {
        try {
            ChessGame clone = (ChessGame) super.clone();
            clone.chessBoard = this.chessBoard.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}