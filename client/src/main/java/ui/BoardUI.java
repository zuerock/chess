package ui;

import java.io.PrintStream;

import chess.*;
import static ui.EscapeSequences.*;

public class BoardUI {
    private static final int DRAWING_SIZE_IN_SQUARES = 10;
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static ChessBoard board;
    private PrintStream out;
    private ChessGame game;
    private ChessGame.TeamColor teamColor;


    public BoardUI() {
    }


    public static void printBoard(PrintStream out) {

        board.resetBoard();

        out.println();
    }

    private static void drawBlackBoard(PrintStream out) {
        drawHeader();
        drawRows();
        drawHeader();
    }


    private static void drawHeader() {
        String headerLine = "";
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        headerLine = switch (team) {
            case WHITE -> "    A   B  C   D   E  F   G   H    ";

            case BLACK -> "    H   G  F   E   D  C   B   A    ";
            default -> headerLine;
        };

        out.print(headerLine);
        out.print(RESET_TEXT_COLOR);
        out.println(RESET_BG_COLOR);
    }


    private static void drawRows() {
        switch (team) {
            case WHITE:
                for (int row = BOARD_SIZE_IN_SQUARES; row >= 1; row--) {
                    for (int col = 0; col < DRAWING_SIZE_IN_SQUARES; col++) {
                        printRow(row, col);
                    }
                    out.println(RESET_BG_COLOR);
                }
                break;

            case BLACK:
                for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {


                    for (int col = DRAWING_SIZE_IN_SQUARES - 1; col >= 0; col--) {
                        printRow(row, col);
                    }
                    out.println(RESET_BG_COLOR);
                }
                break;
        }
    }

    private static String getPiece(int boardRow, int boardCol) {
        ChessPosition position = new ChessPosition(boardRow, boardCol);

        if (board.getPiece(position) == null) {
            return EMPTY;
        }

        ChessPiece piece = board.getPiece(position);
        String pieceString = "";

        switch (piece.getTeamColor()) {
            case WHITE -> {
                switch (piece.getPieceType()) {
                    case KING -> pieceString = WHITE_KING;
                    case QUEEN -> pieceString = WHITE_QUEEN;
                    case ROOK -> pieceString = WHITE_ROOK;
                    case BISHOP -> pieceString = WHITE_BISHOP;
                    case KNIGHT -> pieceString = WHITE_KNIGHT;
                    case PAWN -> pieceString = WHITE_PAWN;
                }
            }
            case BLACK -> {
                switch (piece.getPieceType()) {
                    case KING -> pieceString = BLACK_KING;
                    case QUEEN -> pieceString = BLACK_QUEEN;
                    case ROOK -> pieceString = BLACK_ROOK;
                    case BISHOP -> pieceString = BLACK_BISHOP;
                    case KNIGHT -> pieceString = BLACK_KNIGHT;
                    case PAWN -> pieceString = BLACK_PAWN;
                }
            }
        }
        return pieceString;
    }
}

