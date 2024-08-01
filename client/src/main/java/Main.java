import chess.*;
import ui.BoardUI;

import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        BoardUI.printBoard(System.out);
    }
}