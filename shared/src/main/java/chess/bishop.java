/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.List;

import java.util.Set;


/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */
public class Bishop extends ChessPiece {

    public Bishop(Team team) {
        super(team, PieceType.BISHOP);
    }

    public List<ChessMove> availableMoves(ChessBoard board, ChessPosition currentPosition) {
        List<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPosition currentSquare = board.getSquare(currentPosition.getColumn(), currentPosition.getRow());
        ChessPosition destinationSquare;
        for (ChessPosition position : PIECE_Configurations.BISHOP_MOVES) {
            destinationCoordinate = currentCoord;
            while (BoardUtilities.isValidCoordinate(destinationCoordinate.plus(coord))) {
                destinationCoordinate = destinationCoordinate.plus(coord);
                destinationTile = board.getTile(destinationCoordinate);
                if (!destinationTile.hasPiece()) {
                    possibleMoves.add(new Move(board, currentTile, destinationTile));
                } else {
                    if (destinationTile.getPiece().getTeam() != this.getTeam()) {
                        possibleMoves.add(new Move(board, currentTile, destinationTile));
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        return possibleMoves;
    }

}
