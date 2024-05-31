package result;

public class LoginResult {
    public String username;
    public String authToken;
    public String message;
    public int status;

    public LoginResult(String username, String authToken, String message, int status) {
        this.username = username;
        this.authToken = authToken;
        this.message = message;
        this.status = status;
    }
}
