package ui;

import chess.ChessGame;
import model.GameData;
import request.*;
import result.*;
import server.ServerFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Menu {

    private ServerFacade serverFacade;
    private Scanner sc;
    private Map<Integer,Integer> gameList;
    private GameLoop gameLoop;

    public Menu(int port) {
        serverFacade = new ServerFacade(port);
        sc = new Scanner(System.in);
        gameLoop = new GameLoop(port);
    }

    public void run() {
        while (true) {
            if (serverFacade.getAuthToken() == null) {
                prelogin();
            }
            else {
                postlogin();
            }
        }
    }

    private void prelogin() {
        println("\nWelcome to Dylan's Chess App!");
        println("Enter one of the following commands to continue:");
        println("\tlogin");
        println("\tregister");
        println("\tquit");
        println("For help with these options, type: help");

        boolean valid = false;
        while (!valid) {
            print("\nCommand: ");
            String cmd = sc.nextLine().toLowerCase().trim();
            valid = true;
            switch (cmd) {
                case "login" -> login();
                case "register" -> register();
                case "quit" -> System.exit(0);
                case "help" -> println("Type one of the commands as listed and then hit Enter.");
                default -> {
                    valid = false;
                    println("Unrecognized command, try again.");
                }
            }
        }
    }

    private void login() {
        print("Enter username: ");
        String username = sc.next();
        sc.nextLine();
        print("Enter password: ");
        String password = sc.next();
        sc.nextLine();

        LoginRequest request = new LoginRequest(username, password);
        AuthResult result = serverFacade.login(request);

        if(result.message() != null) {
            println("\n" + result.message());
        }
        else {
            serverFacade.setAuthToken(result.authToken());
            serverFacade.setUsername(result.username());
            println("\nLogged in as: " + result.username());
        }
    }

    private void register() {
        print("Enter email: ");
        String email = sc.next();
        sc.nextLine();

        print("Enter username: ");
        String username = sc.next();
        sc.nextLine();

        print("Enter password: ");
        String password = sc.next();
        sc.nextLine();

        RegisterRequest request = new RegisterRequest(username, password, email);
        AuthResult result = serverFacade.register(request);

        if(result.message() != null) {
            println("\n" + result.message());
        }
        else {
            serverFacade.setAuthToken(result.authToken());
            serverFacade.setUsername(result.username());
            println("\nAccount created, logged in as: " + serverFacade.getUsername());
        }
    }

    private void postlogin() {
        println("\nHello, " + serverFacade.getUsername());
        println("Enter one of the following commands to continue:");
        println("\tlist games");
        println("\tcreate game");
        if (gameList != null) {
            println("\tjoin game");
            println("\tjoin observer");
        }
        println("\tlogout");
        println("For help with these options, type: help");

        boolean valid = false;
        while (!valid) {
            print("\nCommand: ");
            String cmd = sc.nextLine().toLowerCase().trim();
            valid = true;
            switch (cmd) {
                case "list games" -> listGames();
                case "create game" -> createGame();
                case "join game" -> joinGame();
                case "join observer" -> joinObserver();
                case "logout" -> logout();
                case "help" -> println("Type one of the commands (as listed after the colon), then hit Enter.");
                default -> {
                    valid = false;
                    println("Unrecognized command, try again.");
                }
            }
        }
    }

    private void listGames() {
        ListGamesRequest request = new ListGamesRequest(serverFacade.getAuthToken());
        GameListResult result = serverFacade.listGames(request);
        Set<GameData> games = result.games();

        if(result.message() != null) {
            println("\n" + result.message());
        }
        else {
            if(games.isEmpty()) {
                println("No games! Create one to play!");
            }
            else {
                println("\t#\tName\tWhite\tBlack");

                gameList = new HashMap<>();
                int count = 0;

                for (GameData game : games) {
                    ++count;
                    gameList.put(count, game.gameID());
                    println("\t" + count + "\t" + game.gameName() + "\t"
                            + ((game.whiteUsername() == null) ? "-----" : game.whiteUsername()) + "\t"
                            + ((game.blackUsername() == null) ? "-----" : game.blackUsername()));
                }
            }
        }
    }

    private void createGame() {
        print("Enter a game name: ");
        String name = sc.nextLine();

        CreateGameRequest request = new CreateGameRequest(name, serverFacade.getAuthToken());
        GameResult result = serverFacade.createGame(request);
        if(result.message() != null) {
            println("\n" + result.message());
        }
        else {
            println("Created game with name: " + result.gameName());
        }
    }

    private void joinGame() {
        if(gameList != null) {
            print("Enter game #: ");
            int num = sc.nextInt();
            sc.nextLine();

            print("Enter player color: ");
            String color = sc.next().toUpperCase();
            sc.nextLine();

            if (color.equals("WHITE") || color.equals("BLACK")) {

                JoinGameRequest request = new JoinGameRequest(color, gameList.get(num), serverFacade.getAuthToken());
                Result result = serverFacade.joinGame(request);

                if (result.message() != null) {
                    println("\n" + result.message());
                } else {
                    ChessGame.TeamColor perspective = color.equals("WHITE") ?
                            ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    gameLoop.run(serverFacade.getAuthToken(), request.gameID(), perspective, false);
                }
            }
            else {
                println("Invalid color, try white or black.");
            }
        }
        else {
            println("List games first!");
        }
    }

    private void joinObserver() {
        if(gameList != null) {
            print("Enter game #: ");
            int num = sc.nextInt();
            sc.nextLine();

            JoinGameRequest request = new JoinGameRequest(null, gameList.get(num), serverFacade.getAuthToken());
            Result result = serverFacade.joinGame(request);

            if (result.message() != null) {
                println("\n" + result.message());
            }
            else {
                gameLoop.run(serverFacade.getAuthToken(), request.gameID(), ChessGame.TeamColor.WHITE, true);
            }
        }
        else {
            println("List games first!");
        }
    }

    private void logout() {
        LogoutRequest request = new LogoutRequest(serverFacade.getAuthToken());
        Result result = serverFacade.logout(request);

        if (result.message() != null) {
            println("\n" + result.message());
        }
        else {
            serverFacade.setAuthToken(null);
            serverFacade.setUsername(null);
            println("Logged out.");
        }
    }

    private void print(String str) {
        System.out.print(str);
    }

    private void println(String str) {
        System.out.println(str);
    }
}
