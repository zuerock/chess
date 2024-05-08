package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    @Override
    public int hashCode() {
        int hash;
        hash = startPosition.getRow() * 157 + startPosition.getColumn();
        hash += endPosition.getRow() * 31 + endPosition.getColumn();
        if(promotionPiece != null){
            switch (promotionPiece){
                case QUEEN:
                    hash += 11;
                    break;
                case BISHOP:
                    hash += 17;
                    break;
                case KNIGHT:
                    hash += 23;
                    break;
                case ROOK:
                    hash += 29;
                    break;
                default:
                    hash += 1;
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }

        if(!(obj instanceof ChessMove c)){
            return false;
        }

        return this.startPosition.equals(c.startPosition) && this.endPosition.equals(c.endPosition) && promotionPiece == c.promotionPiece;
    }

    @Override
    public String toString() {
        return "Move{" +
                startPosition +
                ", " + endPosition +
                ", " + promotionPiece +
                "}\n";
    }
}
