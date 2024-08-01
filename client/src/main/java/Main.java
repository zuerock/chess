import chess.*;
import ui.*;
import client.ServerFacade;

import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        ServerFacade facade = new ServerFacade(8080);
        ConsoleUI ui = new ConsoleUI(facade);
        ui.run();
    }
}