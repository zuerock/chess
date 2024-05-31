package servicetests;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;
import service.DBService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class APIServiceTests {


    private UserService userService = new UserService();
    private DBService dbService = new DBService();
    private GameService gameService = new GameService();

    private UserDAO userDAO = new UserDAO();
    private AuthDAO authDAO = new AuthDAO();
    private GameDAO gameDAO = new GameDAO();
    @Test
    public void goodRegResult(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        RegisterRequest req = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult testResp = userService.regResult(req, userDAO, authDAO);

        assertEquals(authDAO.authList.get(0).username(), req.username);
        assertEquals(userDAO.userList.get(0).username(), req.username);
    }

    @Test
    public void badRegResult(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterRequest badReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult testResp = userService.regResult(badReq, userDAO, authDAO);

        assertNotEquals(authDAO.authList.get(0).username(), null);
        assertNotEquals(userDAO.userList.get(0).username(), null);
    }

    @Test
    public void goodLoginResult(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        LoginRequest req = new LoginRequest("TestUser", "Pass");
        LoginResult testResp = userService.loginResult(req, userDAO, authDAO);

        assertEquals(userDAO.userList.get(0).password(), req.password);
    }

    @Test
    public void badLoginRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        LoginRequest badReq = new LoginRequest("BadUser", "Pass");
        LoginResult testResp = userService.loginResult(badReq, userDAO, authDAO);

        assertNotEquals(null, userDAO.userList.get(0).password());
    }

    @Test
    public void goodLogoutRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        LogoutResult testResp = userService.logoutResult(authDAO.authList.get(0).authToken(), authDAO);

        assertEquals(authDAO.authList.size(), 0);
    }

    @Test
    public void badLogoutRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        LogoutResult testResp = userService.logoutResult("auth", authDAO);

        assertNotEquals(authDAO.authList.size(), 0);
    }

    @Test
    public void goodClearRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        ClearResult testResp = dbService.clearResult(userDAO, authDAO, gameDAO);

        assertEquals(userDAO.userList.size(), 0);
        assertEquals(authDAO.authList.size(), 0);
        assertEquals(gameDAO.gameList.size(), 0);
    }

    @Test
    public void badClearRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        ClearResult testResp = dbService.clearResult(userDAO, authDAO, gameDAO);

        assertNotEquals(userDAO.userList.size(), 1);
        assertNotEquals(authDAO.authList.size(), 1);
        assertNotEquals(gameDAO.gameList.size(), 1);
    }

    @Test
    public void goodCreateGameRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResult resp = gameService.createGameResult(req, authDAO.authList.get(0).authToken(), authDAO, gameDAO);

        assertEquals(gameDAO.gameList.get(0).gameName(), req.gameName);
    }

    @Test
    public void badCreateGameRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResult resp = gameService.createGameResult(req, "BadAuthToken", authDAO, gameDAO);

        assertNotEquals(req.gameName, gameDAO.gameList.size());
    }

    @Test
    public void goodListGamesRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResult createResp = gameService.createGameResult(createReq, authDAO.authList.get(0).authToken(), authDAO, gameDAO);
        ListGamesResult resp = gameService.listGamesResult(authDAO.authList.get(0).authToken(), authDAO, gameDAO);

        assertEquals(gameDAO.gameList.size(), 1);
    }

    @Test
    public void badListGamesRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResult createResp = gameService.createGameResult(createReq, authDAO.authList.get(0).authToken(), authDAO, gameDAO);
        ListGamesResult resp = gameService.listGamesResult("BadAuthToken", authDAO, gameDAO);

        assertNotEquals(resp.games, gameDAO.gameList);
    }

    @Test
    public void goodJoinGameRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResult createResp = gameService.createGameResult(createReq, authDAO.authList.get(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.gameList.get(0).gameID());
        JoinGameResult resp = gameService.joinGameResult(req, authDAO.authList.get(0).authToken(), authDAO, gameDAO);

        assertEquals(200, resp.status);
    }

    @Test
    public void badJoinGameRequest(){
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResult goodResp = userService.regResult(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResult createResp = gameService.createGameResult(createReq, authDAO.authList.get(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.gameList.get(0).gameID());
        JoinGameResult resp = gameService.joinGameResult(req, "BadAuthToken", authDAO, gameDAO);

        assertNotEquals(200, resp.status);
    }
}
