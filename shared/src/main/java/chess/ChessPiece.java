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
        Collection<ChessMove> possibleMoves = new HashSet<>();

        switch (this.type) {
            case KING:
                possibleMoves = kingMoves(board, myPosition);
                break;
            case QUEEN:
                possibleMoves = queenMoves(board, myPosition);
                break;
            case BISHOP:
                possibleMoves = bishopMoves(board, myPosition);
                break;
            case KNIGHT:
                possibleMoves = knightMoves(board, myPosition);
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

    private Collection<ChessMove> kingMoves(ChessBoard myBoard, ChessPosition currentPosition) {
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

        for (ChessPosition positions : possiblePositions) {
            if (outOfBoundsCheck(positions, myBoard)) {
                continue;
            } else {
                // Check if positions isn't in enemy king's bubble. If it is, continue
                // Column sweeps
                for (int i = positions.getRow() - 2; i < positions.getRow() + 2; i++) {
                    // 2 left column
                    ChessPosition checkPosition = new ChessPosition(i, positions.getColumn() - 2);
                    // Make sure checkPosition is in bounds
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                    // 1 left column
                    checkPosition = new ChessPosition(i, positions.getColumn() - 1);
                    // Make sure checkPosition is in bounds
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                    // 1 right column
                    checkPosition = new ChessPosition(i, positions.getColumn() + 1);
                    // Make sure checkPosition is in bounds
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                    // 2 right column
                    checkPosition = new ChessPosition(i, positions.getColumn() + 2);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                }
                // Row sweeps
                for (int j = positions.getColumn() - 2; j < positions.getColumn() + 2; j++) {
                    // 2 up row
                    ChessPosition checkPosition = new ChessPosition(positions.getRow() + 2, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                    // 1 up row
                    checkPosition = new ChessPosition(positions.getRow() + 1, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                    // 1 down row
                    checkPosition = new ChessPosition(positions.getRow() - 1, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                    // 2 down row
                    checkPosition = new ChessPosition(positions.getRow() - 2, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, positions, checkPosition)) continue;
                }
            }
        }

        return possibleMoves;
    }

    public boolean outOfRange(ChessBoard myBoard, ChessPosition currentPosition, Collection<ChessMove> possibleMoves, ChessPosition positions, ChessPosition checkPosition) {
        if (checkPosition.getRow() < 1 || checkPosition.getRow() > 8 || checkPosition.getColumn() < 1 || checkPosition.getColumn() > 8) {
            return true;
        }
        if (myBoard.getPiece(checkPosition) == null || (myBoard.getPiece(checkPosition) != null && myBoard.getPiece(checkPosition).type != PieceType.KING)) {
            possibleMoves.add(new ChessMove(currentPosition, positions, null));
        }
        return false;
    }

    private Collection<ChessMove> queenMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentColumn = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessMove> potentialDMoves = new HashSet<>();

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

    private Collection<ChessMove> knightMoves(ChessBoard myBoard, ChessPosition currentPosition) {
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

        for (ChessPosition positions : possiblePositions) {
            if (outOfBoundsCheck(positions, myBoard)) {
                continue;
            } else {
                possibleMoves.add(new ChessMove(currentPosition, positions, null));
            }
        }

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

        for (ChessPosition positions : possiblePositions) {
            if (outOfBoundsCheck(positions, myBoard)) {
                continue;
            } else {
                // Check if pawn will move into enemy row. If so, you can do 4 additional moves
                if ((currentPosition.getRow() == 7 && positions.getRow() == 8) || (currentPosition.getRow() == 2 && positions.getRow() == 1)) {
                    possibleMoves.add(new ChessMove(currentPosition, positions, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(currentPosition, positions, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(currentPosition, positions, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(currentPosition, positions, PieceType.ROOK));
                } else {
                    possibleMoves.add(new ChessMove(currentPosition, positions, null));
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

    private boolean outOfBoundsCheck(ChessPosition positions, ChessBoard myBoard) {
        boolean outOfBounds = false;
        // Remove out of bounds positions
        if (positions.getRow() < 1 || positions.getRow() > 8 || positions.getColumn() < 1 || positions.getColumn() > 8) {
            outOfBounds = true;
        }
        // Check if same team's piece is in the way
        else if (myBoard.getPiece(positions) != null && myBoard.getPiece(positions).color == this.color) {
            outOfBounds = true;
        }

        return outOfBounds;
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
