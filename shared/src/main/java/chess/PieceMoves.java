/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import chess.ChessPosition;
import chess.ChessPiece;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */
public class PieceMoves {

    //this ChessPositions means a knıght can make 8 different moves in a possible position. Its current position plus this positions shows its possible move ChessPositions.
    public static ChessPosition[] KNIGHT_MOVES = {new ChessPosition(2, 1), new ChessPosition(-2, 1), new ChessPosition(2, -1), new ChessPosition(-2, -1), new ChessPosition(1, 2), new ChessPosition(-1, 2), new ChessPosition(1, -2), new ChessPosition(-1, -2)};

    //this ChessPositions shows a bishops available move directions from its current position;
    public static ChessPosition[] BISHOP_MOVES = {new ChessPosition(1, 1), new ChessPosition(-1, 1), new ChessPosition(1, -1), new ChessPosition(-1, -1)};

    //this ChessPositions shows a rooks available move directions from its current position;
    public static ChessPosition[] ROOK_MOVES = {new ChessPosition(0, 1), new ChessPosition(0, -1), new ChessPosition(1, 0), new ChessPosition(-1, 0)};

    //this ChessPositions shows a queens available move directions from its current position;
    public static ChessPosition[] QUEEN_MOVES = {new ChessPosition(0, 1), new ChessPosition(0, -1), new ChessPosition(1, 0), new ChessPosition(-1, 0), new ChessPosition(1, 1), new ChessPosition(-1, 1), new ChessPosition(1, -1), new ChessPosition(-1, -1)};

    //this ChessPositions shows a black pawns available movement directions from its current position
    public static ChessPosition[] BLACK_PAWN_NORMAL_MOVES = {new ChessPosition(0, 1)};

    //this ChessPositions shows a white pawns available movement directions from its current position
    public static ChessPosition[] WHITE_PAWN_NORMAL_MOVES = {new ChessPosition(0, -1)};

    //this ChessPositions shows the ChessPosition way that can a white pawn able to attack
    public static ChessPosition[] WHITE_PAWN_ATTACK_MOVES = {new ChessPosition(1, -1), new ChessPosition(-1, -1)};

    //this ChessPositions shows the ChessPosition way that can a black pawn able to attack 
    public static ChessPosition[] BLACK_PAWN_ATTACK_MOVES = {new ChessPosition(1, 1), new ChessPosition(-1, 1)};

    //black pawn able to double move at start. This shows it.
    public static ChessPosition[] BLACK_PAWN_START_MOVES = {new ChessPosition(0, 2)};

    //white pawn able to double move at start. This shows it.
    public static ChessPosition[] WHITE_PAWN_START_MOVES = {new ChessPosition(0, -2)};

    public static int BLACK_PAWNS_START_Y_POS = 1;

    public static int WHITE_PAWNS_START_Y_POS = 6;

    public static Map<Team, Map> PAWN_MOVES; // Here is a map for the 3 different pawn moves.

    static {
        PAWN_MOVES = new HashMap<>();
        Map<String, ChessPosition[]> whitePawnMoves = new HashMap<>();
        Map<String, ChessPosition[]> blackPawnMoves = new HashMap<>();
        whitePawnMoves.put("Normal", WHITE_PAWN_NORMAL_MOVES);
        whitePawnMoves.put("Attack", WHITE_PAWN_ATTACK_MOVES);
        whitePawnMoves.put("Start", WHITE_PAWN_START_MOVES);
        blackPawnMoves.put("Normal", BLACK_PAWN_NORMAL_MOVES);
        blackPawnMoves.put("Attack", BLACK_PAWN_ATTACK_MOVES);
        blackPawnMoves.put("Start", BLACK_PAWN_START_MOVES);
        PAWN_MOVES.put(Team.WHITE, whitePawnMoves);
        PAWN_MOVES.put(Team.BLACK, blackPawnMoves);

    }

    public static int getPawnStartPosY(Team team) {
        if (team == Team.WHITE) {
            return WHITE_PAWNS_START_Y_POS;
        } else {
            return BLACK_PAWNS_START_Y_POS;
        }
    }
}
