package ui;

import chess.ChessGame;
import chess.ChessPiece;
import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final String LABEL = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_FAINT;
    private static final String WHITE = SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD;
    private static final String BLACK = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + SET_TEXT_BOLD;

    private static final String[][] STYLES = {
            {LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL},
            {LABEL, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, LABEL},
            {LABEL, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, LABEL},
            {LABEL, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, LABEL},
            {LABEL, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, LABEL},
            {LABEL, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, LABEL},
            {LABEL, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, LABEL},
            {LABEL, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, LABEL},
            {LABEL, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, BLACK, WHITE, LABEL},
            {LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL, LABEL}
    };

    private static final String[][] TEXT = new String[10][10];

    private static final String SPACE = " ";

    private static ChessBoard instance;

    private ChessBoard() {}

    public static ChessBoard getInstance() {
        if (instance == null) {
            instance = new ChessBoard();
        }
        return instance;
    }

    public void printChessBoard(chess.ChessBoard board, ChessGame.TeamColor perspective) {
        ChessPiece[][] pieces = board.getPieces();
        String[] rowLabels;
        String[] colLabels;

        if (perspective == ChessGame.TeamColor.WHITE) {
            rowLabels = toStringArray(" abcdefgh ");
            colLabels = toStringArray(" 87654321 ");
        }
        else {
            rowLabels = toStringArray(" hgfedcba ");
            colLabels = toStringArray(" 12345678 ");
        }

        TEXT[0] = rowLabels;
        TEXT[9] = rowLabels;
        for (int i = 0; i < 10; ++i) {
            TEXT[i][0] = colLabels[i];
            TEXT[i][9] = colLabels[i];
        }

        for (int r = 0; r < 8; ++r) {
            for (int c = 0; c < 8; ++c) {
                if(perspective == ChessGame.TeamColor.WHITE) {
                    TEXT[r+1][c+1] = toCharacterRepresentation(pieces[7-r][c]);
                }
                else {
                    TEXT[r+1][c+1] = toCharacterRepresentation(pieces[r][7-c]);
                }
            }
        }

        for (int r = 0; r < 10; ++r) {
            for (int c = 0; c < 10; ++c) {
                formatPrint(r, c);
            }
            nextLine();
        }
    }

    private String[] toStringArray(String str) {
        String[] arr = new String[str.length()];
        for(int i = 0; i < str.length(); ++i) {
            arr[i] = str.substring(i,i+1);
        }
        return arr;
    }

    private String toCharacterRepresentation(ChessPiece piece) {
        if(piece == null) {
            return SPACE;
        }
        return switch (piece.getPieceType()) {
            case KING -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? "K" : "k";
            case QUEEN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? "Q" : "q";
            case ROOK -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? "R" : "r";
            case BISHOP -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? "B" : "b";
            case KNIGHT -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? "N" : "n";
            case PAWN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    ? "P" : "p";
        };
    }

    private void formatPrint(int r, int c) {
        System.out.print(STYLES[r][c]);
        System.out.print(SPACE + TEXT[r][c] + SPACE);
    }

    private void nextLine() {
        System.out.println(RESET_ALL_FORMATTING);
    }
}
