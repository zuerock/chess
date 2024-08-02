package ui;

import java.io.PrintStream;
import java.util.Collection;

import chess.*;

import static ui.EscapeSequences.*;

public class BoardUI {
    private static final int DRAWING_SIZE_IN_SQUARES = 10;
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static ChessBoard board;
    private static PrintStream out;
    private static ChessGame game;
    private static ChessGame.TeamColor teamColor;


    public BoardUI(PrintStream outStream, ChessGame gameState) {
        game = gameState;
        out = outStream;
        board = game.getBoard();
    }


    public static void printBoard() { // print board for current team
        teamColor = game.getTeamTurn();
        printBoard(game.getTeamTurn());
    }

    public static void printBoard(ChessGame.TeamColor givenTeam) { // if given a specific team, print that board instead
        teamColor = givenTeam;

        out.println();
        drawHeader();
        prepareRows();
        drawHeader();
    }


    private static void drawHeader() {
        String headerLine = "";
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        headerLine = switch (teamColor) {
            case WHITE -> "    A   B  C   D   E  F   G   H    ";

            case BLACK -> "    H   G  F   E   D  C   B   A    ";
            default -> headerLine;
        };

        out.print(headerLine);
        out.print(RESET_TEXT_COLOR);
        out.println(RESET_BG_COLOR);
    }


    private static void prepareRows() {
        switch (teamColor) {
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

    private static void printRow(int row, int col) {
        // print side header squares
        if (col == 0 || col == DRAWING_SIZE_IN_SQUARES - 1) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);

            int paddingSize = (EMPTY.length() - 1) / 2; // Subtract 1 for the row number itself
            String sideSquare = String.format("%" + paddingSize + "s" + row + "%" + paddingSize + "s", "", "");
            out.print(sideSquare);
        }

        // print board row
        else {
            // set square color
            if (row % 2 == 0) {
                if (col % 2 == 0) {
                    out.print(SET_BG_COLOR_BLACK);
                } else {
                    out.print(SET_BG_COLOR_WHITE);
                }
            } else {
                if (col % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
            }

            String piece = getPiece(row, col);
            out.print(piece);
            out.print(RESET_TEXT_COLOR);
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
    public void printValidMoves(Collection<ChessPosition> endPositions, ChessPosition pos, ChessGame.TeamColor teamState) {
        out.println();
        teamColor = teamState;

        // draw board
        drawHeader();
        drawValRows(endPositions, pos);
        drawHeader();
    }

    private void drawValRows(Collection<ChessPosition> endPositions, ChessPosition pos) {
        switch (teamColor) {
            case WHITE:
                for (int row = BOARD_SIZE_IN_SQUARES; row >= 1; row--) {
                    for (int col = 0; col < DRAWING_SIZE_IN_SQUARES; col++) {
                        printValRow(row, col, endPositions, pos);
                    }
                    out.println(RESET_BG_COLOR);
                }
                break;

            case BLACK:
                for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {


                    for (int col = DRAWING_SIZE_IN_SQUARES - 1; col >= 0; col--) {
                        printValRow(row, col, endPositions, pos);
                    }
                    out.println(RESET_BG_COLOR);
                }
                break;
        }
    }
    private void printValRow(int row, int col, Collection<ChessPosition> endPositions, ChessPosition pos) {
        // print side header squares
        if (col == 0 || col == DRAWING_SIZE_IN_SQUARES - 1) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);

            int paddingSize = (EMPTY.length() - 1) / 2; // Subtract 1 for the row number itself
            String sideSquare = String.format("%" + paddingSize + "s" + row + "%" + paddingSize + "s", "", "");
            out.print(sideSquare);
        }

        // print board row
        else {
            // set square color
            if (row % 2 == 0) {
                if (col % 2 == 0) {
                    if (isMove(row, col, endPositions)) out.print(SET_BG_COLOR_DARK_GREEN);
                    else if (pos.getRow() == row && pos.getColumn() == col) out.print(SET_BG_COLOR_YELLOW);
                    else out.print(SET_BG_COLOR_BLACK);
                } else {
                    if (isMove(row, col, endPositions)) out.print(SET_BG_COLOR_GREEN);
                    else if (pos.getRow() == row && pos.getColumn() == col) out.print(SET_BG_COLOR_YELLOW);
                    else out.print(SET_BG_COLOR_WHITE);
                }
            } else {
                if (col % 2 == 0) {
                    if (isMove(row, col, endPositions)) out.print(SET_BG_COLOR_GREEN);
                    else if (pos.getRow() == row && pos.getColumn() == col) out.print(SET_BG_COLOR_YELLOW);
                    else out.print(SET_BG_COLOR_WHITE);
                } else {
                    if (isMove(row, col, endPositions)) out.print(SET_BG_COLOR_DARK_GREEN);
                    else if (pos.getRow() == row && pos.getColumn() == col) out.print(SET_BG_COLOR_YELLOW);
                    else out.print(SET_BG_COLOR_BLACK);
}

