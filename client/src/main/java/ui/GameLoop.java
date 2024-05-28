package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.CommandHandler;
import websocket.WebSocketFacade;

import java.util.Scanner;

public class GameLoop implements CommandHandler {
    private ChessBoard chessBoard;
    private int port;
    private WebSocketFacade wsf;
    Scanner sc = new Scanner(System.in);

    private String authToken;
    private int gameID;
    private ChessGame.TeamColor perspective;
    private ChessGame game;

    public GameLoop(int port) {
        chessBoard = ChessBoard.getInstance();
        this.port = port;
        wsf = new WebSocketFacade(8080, this);
    }

    public void run(String authToken, int gameID, ChessGame.TeamColor perspective, boolean isObserving) {
        this.perspective = perspective;
        this.authToken = authToken;
        this.gameID = gameID;

        if(isObserving) {
            wsf.joinObserver(authToken, gameID);
        }
        else {
            wsf.joinPlayer(authToken, gameID, perspective);
        }

        printHelp();

        String next = "";
        while (!next.equals("leave")) {
            next = sc.next().toLowerCase();
            sc.nextLine();

            switch(next) {
                case "make" -> makeMove();
                case "highlight" -> highlightLegalMoves();
                case "redraw" -> redrawChessBoard();
                case "resign" -> resign();
                case "leave" -> leave();
                case "help" -> printHelp();
                default -> println("Invalid command, type 'help' for options.");
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = (NotificationMessage) notification;
                println(notificationMessage.getMessage());
            }
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = (LoadGameMessage) notification;
                game = loadGameMessage.getGame();
                println("");
                chessBoard.printChessBoard(game.getBoard(), perspective);
            }
            case ERROR -> {
                ErrorMessage errorMessage = (ErrorMessage) notification;
                println("Error: "+errorMessage.getErrorMessage());
            }
        }
    }

    private void makeMove() {
        print("Enter start position: ");
        String start = sc.next().toLowerCase();
        sc.nextLine();
        print("Enter end position: ");
        String end = sc.next().toLowerCase();
        sc.nextLine();

        int startRow = Character.getNumericValue(start.charAt(1));
        int startCol = 1+(start.charAt(0)-'a');

        int endRow = Character.getNumericValue(end.charAt(1));
        int endCol = 1+(end.charAt(0)-'a');

        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);

        ChessPiece.PieceType type = null;
        if(startRow>=1 && startRow<=8 && startCol>=1 && startCol<=8
                && game.getBoard().getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN
                && (endRow == 1 || endRow == 8)) {
            print("Enter promotion piece type: ");
            String typeString = sc.next().toLowerCase();
            sc.nextLine();
            switch (typeString) {
                case "queen" -> type = ChessPiece.PieceType.QUEEN;
                case "rook" -> type = ChessPiece.PieceType.ROOK;
                case "bishop" -> type = ChessPiece.PieceType.BISHOP;
                case "knight" -> type = ChessPiece.PieceType.KNIGHT;
            }
        }

        ChessMove move = new ChessMove(startPosition, endPosition, type);

        wsf.makeMove(authToken, gameID, move);
    }
    private void highlightLegalMoves() {

    }
    private void redrawChessBoard() {
        chessBoard.printChessBoard(game.getBoard(), perspective);
    }
    private void resign() {
        wsf.resign(authToken, gameID);
    }
    private void leave() {
        wsf.leave(authToken, gameID);
    }

    private void printHelp() {
        println("\nType one of the following commands and hit enter. (use just first word for shorthand):");
        println("\tmake move");
        println("\thighlight legal moves");
        println("\tredraw chess board");
        println("\tresign");
        println("\tleave");
        println("");
    }

    private void print(String str) {
        System.out.print(str);
    }

    private void println(String str) {
        System.out.println(str);
    }
}