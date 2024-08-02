package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.*;

import chess.*;
import client.ServerFacade;
import model.AuthData;
import model.GameData;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ConsoleUI {
    private final ServerFacade facade;
    private ChessBoard board;
    private ChessGame game;
    private AuthData auth;
    private Map<Integer, GameData> numberedList;

    boolean loggedIn;

    PrintStream out;
    Scanner in;
    private ChessGame.TeamColor teamColor;

    public ConsoleUI(ServerFacade facade) {
        this.facade = facade;
        board = new ChessBoard();
        game = new ChessGame();
        loggedIn = false;
        numberedList = new HashMap<>();
    }

    // prepare to move


    public void run() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String input = "";
        in = new Scanner(System.in);

        out.print(ERASE_SCREEN);
        out.println("♕ Welcome to 240 chess. Type 'help' to get started. ♕");

        while (!Objects.equals(input, "quit")) {
            if (!(loggedIn)) {
                out.print("[LOGGED_OUT] >>> ");
                input = in.nextLine();
                preLogin(input);
            } else {
                out.print("[LOGGED_IN] >>> ");
                input = in.nextLine();
                postLogin(input);
            }
        }
        out.println("Hope you had fun, bye!");
    }


    private void preLogin(String input) {
        switch (input) {
            case "register":
                register();
                break;
            case "login":
                login();
                break;
            case "help":
                help();
                break;
            case "quit":
                break;
            default:
                out.println("Invalid command. Type 'help' for a list of commands.");
        }
    }

    private void postLogin(String input) {
        switch (input) {
            case "create":
                create();
                break;
            case "list":
                list();
                break;
            case "play":
                play();
                break;
            case "observe":
                observe();
                break;
            case "logout":
                logout();
                break;
            case "help":
                help();
                break;
            case "quit":
                break;
            default:
                out.println("Invalid command. Type 'help' for a list of commands.");
        }
    }

    private void redraw() {
        new BoardUI(out, game);
        BoardUI.printBoard();
    }

    private void makeMove() {
        try {
            out.println("Which piece would you like to move? (ex: E4): ");
            String inputStart = in.nextLine();
            ChessPosition start = toPos(inputStart);

            out.println("Where would you like to move it? (ex: E5): ");
            String inputEnd = in.nextLine();
            ChessPosition end = toPos(inputEnd);

            ChessMove move = new ChessMove(start, end, null);
            facade.makeMove(move);
            new BoardUI(out, game);
            BoardUI.printBoard();
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void inGame(String input){

        switch (input) {
            case "redraw":
                //redraw();
                break;
            case "make move":
               // makeMove();
                break;
            case "resign":
                //resign();
                break;
            case "highlight":
                //highlight();
                break;
            case "help":
                help();
                break;
            case "exit", "quit", "leave":
                break;
            default:
                out.println("Invalid command. Type 'help' for a list of commands. //INGAME");
        }
    }


    private void logout() {
        try {
            facade.logout(auth.authToken());
            out.println("Successfully logged out.");
            loggedIn = false;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void observe() {
        out.print("Enter the ID of the game you'd like to observe: ");
        String input = in.nextLine();


        if (isInList(input)) {
            int gameID = Integer.parseInt(input);
            GameData listedGame = numberedList.get(gameID);
            game = listedGame.game();

            new BoardUI(out, game);
        }
    }

    private void play() {
        out.print("Enter either the number or ID of the game you'd like to join: ");
        String input = in.nextLine();

        if (isInList(input)) {
            // get game ID from list
            int gameNum = Integer.parseInt(input);
            GameData listedGame = numberedList.get(gameNum);
            int gameID = listedGame.gameID();

            // get desired team
            out.print("Please enter your desired team (WHITE or BLACK): ");
            String givenTeam = in.nextLine();
            String team = givenTeam.toUpperCase(); // in case they give a lowercase team
            // check if input is valid
            if (!team.equals("WHITE") && !team.equals("BLACK")) {
                out.println("Invalid team. Please enter either 'WHITE' or 'BLACK'.");
                return;
            }

            try {
                facade.joinGame(auth.authToken(), team, gameID);
                out.println("Joined game [" + gameID + "] as the " + team + " player");

                game = listedGame.game();

                if (team.equals("WHITE")){
                    teamColor = WHITE;
                    new BoardUI(out, game).printBoard(teamColor);
                }
                else{
                    teamColor = ChessGame.TeamColor.BLACK;
                    new BoardUI(out, game).printBoard(teamColor);
                }
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }
    }

    private void list() {
        try {
            var gamesList = facade.listGames(auth.authToken());
            int i = 1;
            if (numberedList != null) numberedList.clear();
            out.println("Games:");
            for (var game : gamesList) {
                out.println(i + " -- Name: " + game.gameName() + ", ID: " + game.gameID() + ", White player: " + game.whiteUsername() + ", Black player: " + game.blackUsername());
                numberedList.put(i, game);
                i++;
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void create() {
        out.print("Enter a new game name: ");
        String gameName = in.nextLine();
        try {
            int id = facade.createGame(auth.authToken(), gameName);
            out.println("Game created with ID: " + id);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    public void register() {
        out.print("Enter new username: ");
        String username = in.nextLine();
        out.print("Enter new password: ");
        String password = in.nextLine();
        out.print("Enter new email: ");
        String email = in.nextLine();

        try {
            auth = facade.register(username, password, email);
            out.println("Registration successful.  Hello, " + username + "!");
            loggedIn = true;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    public void login() {
        out.print("Enter username: ");
        String username = in.nextLine();
        out.print("Enter password: ");
        String password = in.nextLine();

        try {
            auth = facade.login(username, password);
            out.println("Login successful.  Hello, " + username + "!");
            loggedIn = true;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    public void help() {
        if (loggedIn) {
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "create" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - make a new game");
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - get a list of games");
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "play" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - join a game as either BLACK or WHITE");
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "observe" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - view a game as a spectator");
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - logout and return to the main menu");
        } else {
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "register" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - to create an account");
            out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "login" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - to login");
        }
        out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - to display this help menu");
        out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR + " - to exit this program");
    }

    private boolean isInList(String input) {
        int gameID = 0;

        try {
            gameID = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            out.println("Invlaid input. Please try again.");
            return false;
        }
        if (!numberedList.containsKey(gameID)){
            out.println("Invalid game. Please enter a valid number (try 'list').");
            return false;
        }
        return true;
    }
}
