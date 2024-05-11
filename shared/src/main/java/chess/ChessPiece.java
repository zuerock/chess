package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessPiece implements Cloneable{

    private final PieceType type;
    private final ChessGame.TeamColor color;

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

    public ChessGame.TeamColor getTeamColor() { return this.color; }

    public PieceType getPieceType() { return this.type; }

    // Return all possible moves given a piece's position on the board
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves;

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
        int currentCol = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessPosition> potentialPositions = new HashSet<>();

        // UpLeft
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol - 1));
        // Up
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol));
        // UpRight
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol + 1));
        // Left
        potentialPositions.add(new ChessPosition(currentRow, currentCol - 1));
        // Right
        potentialPositions.add(new ChessPosition(currentRow, currentCol + 1));
        // DownLeft
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol - 1));
        // Down
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol));
        // DownRight
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol + 1));

        for (ChessPosition position : potentialPositions) {
            if (obCheck(position, myBoard)) {
                continue;
            } else {
                // Check if position isn't in enemy king's bubble. If it is, continue
                // Column sweeps
                for (int i = position.getRow() - 2; i < position.getRow() + 2; i++) {
                    // 2 left column
                    ChessPosition checkPos = new ChessPosition(i, position.getColumn() - 2);
                    // Make sure checkPos is in bounds
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                    // 1 left column
                    checkPos = new ChessPosition(i, position.getColumn() - 1);
                    // Make sure checkPos is in bounds
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                    // 1 right column
                    checkPos = new ChessPosition(i, position.getColumn() + 1);
                    // Make sure checkPos is in bounds
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                    // 2 right column
                    checkPos = new ChessPosition(i, position.getColumn() + 2);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                }
                // Row sweeps
                for (int j = position.getColumn() - 2; j < position.getColumn() + 2; j++) {
                    // 2 up row
                    ChessPosition checkPos = new ChessPosition(position.getRow() + 2, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                    // 1 up row
                    checkPos = new ChessPosition(position.getRow() + 1, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                    // 1 down row
                    checkPos = new ChessPosition(position.getRow() - 1, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                    // 2 down row
                    checkPos = new ChessPosition(position.getRow() - 2, j);
                    if (outOfRange(myBoard, currentPosition, possibleMoves, position, checkPos)) continue;
                }
            }
        }

        return possibleMoves;
    }

    public boolean outOfRange(ChessBoard myBoard, ChessPosition currentPosition, Collection<ChessMove> possibleMoves, ChessPosition position, ChessPosition checkPos) {
        if (checkPos.getRow() < 1 || checkPos.getRow() > 8 || checkPos.getColumn() < 1 || checkPos.getColumn() > 8) {
            return true;
        }
        if (myBoard.getPiece(checkPos) == null || (myBoard.getPiece(checkPos) != null && myBoard.getPiece(checkPos).type != PieceType.KING)) {
            possibleMoves.add(new ChessMove(currentPosition, position, null));
        }
        return false;
    }

    private Collection<ChessMove> queenMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves;
        Collection<ChessMove> potentialDMoves;

        possibleMoves = rowColCheck(myBoard, currentPosition, currentRow, currentCol);
        potentialDMoves = diagCheck(myBoard, currentPosition, currentRow, currentCol);
        possibleMoves.addAll(potentialDMoves);
        return possibleMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        return diagCheck(myBoard, currentPosition, currentRow, currentCol);
    }

    private Collection<ChessMove> knightMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessPosition> potentialPositions = new HashSet<>();

        // Each move is formatted Dir1Dir2 where Dir1 moves 1 space and Dir2 moves 2
        // UpLeft
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol - 2));
        // LeftUp
        potentialPositions.add(new ChessPosition(currentRow + 2, currentCol - 1));
        // UpRight
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol + 2));
        // RightUp
        potentialPositions.add(new ChessPosition(currentRow + 2, currentCol + 1));
        // DownLeft
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol - 2));
        // LeftDown
        potentialPositions.add(new ChessPosition(currentRow - 2, currentCol - 1));
        // DownRight
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol + 2));
        // RightDown
        potentialPositions.add(new ChessPosition(currentRow - 2, currentCol + 1));

        for (ChessPosition position : potentialPositions) {
            if (obCheck(position, myBoard)) {
                continue;
            } else {
                possibleMoves.add(new ChessMove(currentPosition, position, null));
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        return rowColCheck(myBoard, currentPosition, currentRow, currentCol);
    }

    private Collection<ChessMove> pawnMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        Collection<ChessPosition> potentialPositions = new HashSet<>();
        Collection<ChessMove> possibleMoves = new HashSet<>();

        // If pawn is in start position, it can move 2 squares
        // If pawn moves into opposing team's back line, it can receive a promotion

        // White team
        if (myBoard.getPiece(currentPosition).color == ChessGame.TeamColor.WHITE) {
            // Look up once
            if (currentRow < 8) {
                ChessPosition above = new ChessPosition(currentRow + 1, currentCol);
                if (myBoard.getPiece(above) == null) {
                    potentialPositions.add(above);
                    ChessPosition twoAbove = new ChessPosition(currentRow + 2, currentCol);
                    if (currentRow == 2 && myBoard.getPiece(twoAbove) == null) {
                        // Look up twice if once is safe
                        potentialPositions.add(twoAbove);
                    }
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            if (currentRow < 8 && currentCol > 1) {
                ChessPosition upLeft = new ChessPosition(currentRow + 1, currentCol - 1);
                if (myBoard.getPiece(upLeft) != null && myBoard.getPiece(upLeft).color != this.color) {
                    potentialPositions.add(upLeft);
                }
            }
            if (currentRow < 8 && currentCol < 8) {
                ChessPosition upRight = new ChessPosition(currentRow + 1, currentCol + 1);
                if (myBoard.getPiece(upRight) != null && myBoard.getPiece(upRight).color != this.color) {
                    potentialPositions.add(upRight);
                }
            }
        }
        // Black team
        else if (myBoard.getPiece(currentPosition).color == ChessGame.TeamColor.BLACK) {
            // Look down once
            if (currentRow > 1) {
                ChessPosition below = new ChessPosition(currentRow - 1, currentCol);
                if (myBoard.getPiece(below) == null) {
                    potentialPositions.add(below);
                    ChessPosition twoBelow = new ChessPosition(currentRow - 2, currentCol);
                    if (currentRow == 7 && myBoard.getPiece(twoBelow) == null) {
                        // Look down twice if once is safe
                        potentialPositions.add(twoBelow);
                    }
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            if (currentRow > 1 && currentCol > 1) {
                ChessPosition downLeft = new ChessPosition(currentRow - 1, currentCol - 1);
                if (myBoard.getPiece(downLeft) != null && myBoard.getPiece(downLeft).color != this.color) {
                    potentialPositions.add(downLeft);
                }
            }
            if (currentRow > 1 && currentCol < 8) {
                ChessPosition downRight = new ChessPosition(currentRow - 1, currentCol + 1);
                if (myBoard.getPiece(downRight) != null && myBoard.getPiece(downRight).color != this.color) {
                    potentialPositions.add(downRight);
                }
            }
        }

        for (ChessPosition position : potentialPositions) {
            if (obCheck(position, myBoard)) {
                continue;
            } else {
                // Check if pawn will move into enemy row. If so, you can do 4 additional moves
                if ((currentPosition.getRow() == 7 && position.getRow() == 8) || (currentPosition.getRow() == 2 && position.getRow() == 1)) {
                    possibleMoves.add(new ChessMove(currentPosition, position, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(currentPosition, position, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(currentPosition, position, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(currentPosition, position, PieceType.ROOK));
                } else {
                    possibleMoves.add(new ChessMove(currentPosition, position, null));
                }
            }
        }

        return possibleMoves;
    }

    // HELPERS
    private Collection<ChessMove> rowColCheck(ChessBoard myBoard, ChessPosition currentPosition, int currentRow, int currentCol) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        // Row checking UP
        for (int i = currentRow + 1; i <= 8; i++) {
            ChessPosition potentialPosition = new ChessPosition(i, currentCol);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Row checking DOWN
        for (int i = currentRow - 1; i > 0; i--) {
            ChessPosition potentialPosition = new ChessPosition(i, currentCol);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Column checking LEFT
        for (int j = currentCol - 1; j > 0; j--) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Column checking RIGHT
        for (int j = currentCol + 1; j <= 8; j++) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        return possibleMoves;
    }

    private Collection<ChessMove> diagCheck(ChessBoard myBoard, ChessPosition currentPosition, int currentRow, int currentCol) {
        Collection<ChessMove> possibleMoves = new HashSet<>();

        // Diagonal checking UP-LEFT
        for (int i = currentRow + 1, j = currentCol - 1; i <= 8 && j > 0; i++, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking UP-RIGHT
        for (int i = currentRow + 1, j = currentCol + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking DOWN-LEFT
        for (int i = currentRow - 1, j = currentCol - 1; i > 0 && j > 0; i--, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking DOWN-RIGHT
        for (int i = currentRow - 1, j = currentCol + 1; i > 0 && j <= 8; i--, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPosition, potentialPosition, possibleMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }

        return possibleMoves;
    }

    public void processPotMove(ChessBoard myBoard, ChessPosition currPos, ChessPosition potPos, Collection<ChessMove> possibleMoves) {
        ChessPiece potPiece = myBoard.getPiece(potPos);
        if (potPiece != null) {
            if (potPiece.color != this.color) {
                ChessMove potMove = new ChessMove(currPos, potPos, null);
                possibleMoves.add(potMove);
            }
            return;
        }

        ChessMove potMove = new ChessMove(currPos, potPos, null);
        possibleMoves.add(potMove);
    }

    private boolean obCheck(ChessPosition position, ChessBoard myBoard) {
        boolean ob = false;
        // Remove out of bounds positions
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            ob = true;
        }
        // Check if same team's piece is in the way
        else if (myBoard.getPiece(position) != null && myBoard.getPiece(position).color == this.color) {
            ob = true;
        }

        return ob;
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

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
