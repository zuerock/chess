package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessPiece {

    private PieceType type;
    private ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        this.type = type;
        this.color = color;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    public PieceType getPieceType() {
        return this.type;
    }

    // Return all possible moves given a piece's position on the board
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves;

        switch (this.type) {
            case KING:
                possibleMoves = kingMoves(myPosition);
                break;
            case QUEEN:
                possibleMoves = queenMoves(board, myPosition);
                break;
            case BISHOP:
                possibleMoves = bishopMoves(board, myPosition);
                break;
            case KNIGHT:
                possibleMoves = knightMoves(myPosition);
                break;
            case ROOK:
                possibleMoves = rookMoves(board, myPosition);
                break;
            case PAWN:
                possibleMoves = pawnMoves(board, myPosition);
                break;
            default:
                return null;
        }

        return possibleMoves;
    }

    private Collection<ChessMove> kingMoves(ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessPosition> possiblePositions = new HashSet<>();

        // UpLeft
        possiblePositions.add(new ChessPosition(currentRow + 1, currentColumn - 1));
        // Up
        possiblePositions.add(new ChessPosition(currentRow + 1, currentColumn));
        // UpRight
        possiblePositions.add(new ChessPosition(currentRow + 1, currentColumn + 1));
        // Left
        possiblePositions.add(new ChessPosition(currentRow, currentColumn - 1));
        // Right
        possiblePositions.add(new ChessPosition(currentRow, currentColumn + 1));
        // DownLeft
        possiblePositions.add(new ChessPosition(currentRow - 1, currentColumn - 1));
        // Down
        possiblePositions.add(new ChessPosition(currentRow - 1, currentColumn));
        // DownRight
        possiblePositions.add(new ChessPosition(currentRow - 1, currentColumn + 1));



        return possibleMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves;
        Collection<ChessMove> potentialDMoves;

        possibleMoves = rowColCheck(myBoard, currentPosition, currentRow, currentColumn);
        potentialDMoves = diagCheck(myBoard, currentPosition, currentRow, currentColumn);
        possibleMoves.addAll(potentialDMoves);
        return possibleMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        return diagCheck(myBoard, currentPosition, currentRow, currentColumn);
    }

    private Collection<ChessMove> knightMoves(ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessPosition> possiblePositions = new HashSet<>();

        // Each move is formatted Dir1Dir2 where Dir1 moves 1 space and Dir2 moves 2
        // UpLeft
        possiblePositions.add(new ChessPosition(currentRow + 1, currentColumn - 2));
        // LeftUp
        possiblePositions.add(new ChessPosition(currentRow + 2, currentColumn - 1));
        // UpRight
        possiblePositions.add(new ChessPosition(currentRow + 1, currentColumn + 2));
        // RightUp
        possiblePositions.add(new ChessPosition(currentRow + 2, currentColumn + 1));
        // DownLeft
        possiblePositions.add(new ChessPosition(currentRow - 1, currentColumn - 2));
        // LeftDown
        possiblePositions.add(new ChessPosition(currentRow - 2, currentColumn - 1));
        // DownRight
        possiblePositions.add(new ChessPosition(currentRow - 1, currentColumn + 2));
        // RightDown
        possiblePositions.add(new ChessPosition(currentRow - 2, currentColumn + 1));

        return possibleMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        return rowColCheck(myBoard, currentPosition, currentRow, currentColumn);
    }

    private Collection<ChessMove> pawnMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        Collection<ChessPosition> possiblePositions = new HashSet<>();
        Collection<ChessMove> possibleMoves = new HashSet<>();

        // If pawn is in start position, it can move 2 squares
        // If pawn moves into opposing team's back line, it can receive a promotion

        // White team
        if (myBoard.getPiece(currentPosition).color == ChessGame.TeamColor.WHITE) {
            // Look up once
            if (currentRow < 8) {
                ChessPosition above = new ChessPosition(currentRow + 1, currentColumn);
                if (myBoard.getPiece(above) == null) {
                    possiblePositions.add(above);
                    ChessPosition twoAbove = new ChessPosition(currentRow + 2, currentColumn);
                    if (currentRow == 2 && myBoard.getPiece(twoAbove) == null) {
                        // Look up twice if once is safe
                        possiblePositions.add(twoAbove);
                    }
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            if (currentRow < 8 && currentColumn > 1) {
                ChessPosition upLeft = new ChessPosition(currentRow + 1, currentColumn - 1);
                if (myBoard.getPiece(upLeft) != null && myBoard.getPiece(upLeft).color != this.color) {
                    possiblePositions.add(upLeft);
                }
            }
            if (currentRow < 8 && currentColumn < 8) {
                ChessPosition upRight = new ChessPosition(currentRow + 1, currentColumn + 1);
                if (myBoard.getPiece(upRight) != null && myBoard.getPiece(upRight).color != this.color) {
                    possiblePositions.add(upRight);
                }
            }
        }
        // Black team
        else if (myBoard.getPiece(currentPosition).color == ChessGame.TeamColor.BLACK) {
            // Look down once
            if (currentRow > 1) {
                ChessPosition below = new ChessPosition(currentRow - 1, currentColumn);
                if (myBoard.getPiece(below) == null) {
                    possiblePositions.add(below);
                    ChessPosition twoBelow = new ChessPosition(currentRow - 2, currentColumn);
                    if (currentRow == 7 && myBoard.getPiece(twoBelow) == null) {
                        // Look down twice if once is safe
                        possiblePositions.add(twoBelow);
                    }
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            if (currentRow > 1 && currentColumn > 1) {
                ChessPosition downLeft = new ChessPosition(currentRow - 1, currentColumn - 1);
                if (myBoard.getPiece(downLeft) != null && myBoard.getPiece(downLeft).color != this.color) {
                    possiblePositions.add(downLeft);
                }
            }
            if (currentRow > 1 && currentColumn < 8) {
                ChessPosition downRight = new ChessPosition(currentRow - 1, currentColumn + 1);
                if (myBoard.getPiece(downRight) != null && myBoard.getPiece(downRight).color != this.color) {
                    possiblePositions.add(downRight);
                }
            }
        }

        return possibleMoves;
    }

    // HELPERS
    private Collection<ChessMove> rowColCheck(ChessBoard myBoard, ChessPosition currentPosition, int currentRow, int currentColumn) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        // Row checking UP
        for (int i = currentRow + 1; i <= 8; i++) {
            ChessPosition potentialPosition = new ChessPosition(i, currentColumn);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Row checking DOWN
        for (int i = currentRow - 1; i > 0; i--) {
            ChessPosition potentialPosition = new ChessPosition(i, currentColumn);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Column checking LEFT
        for (int j = currentColumn - 1; j > 0; j--) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Column checking RIGHT
        for (int j = currentColumn + 1; j <= 8; j++) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        return possibleMoves;
    }

    private Collection<ChessMove> diagCheck(ChessBoard myBoard, ChessPosition currentPosition, int currentRow, int currentColumn) {
        Collection<ChessMove> possibleMoves = new HashSet<>();

        // Diagonal checking UP-LEFT
        for (int i = currentRow + 1, j = currentColumn - 1; i <= 8 && j > 0; i++, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking UP-RIGHT
        for (int i = currentRow + 1, j = currentColumn + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking DOWN-LEFT
        for (int i = currentRow - 1, j = currentColumn - 1; i > 0 && j > 0; i--, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking DOWN-RIGHT
        for (int i = currentRow - 1, j = currentColumn + 1; i > 0 && j <= 8; i--, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPossibleMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }

        return possibleMoves;
    }

    public void processPossibleMove(ChessBoard myBoard, ChessPosition currentPosition, ChessPosition possiblePosition, Collection<ChessMove> possibleMoves) {
        ChessPiece possiblePiece = myBoard.getPiece(possiblePosition);
        if (possiblePiece != null) {
            if (possiblePiece.color != this.color) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
            return;
        }

        ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
        possibleMoves.add(possibleMove);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", color=" + color +
                '}';
    }
}