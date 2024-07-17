package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    private HashMap<String, UserData> users;

    public boolean isEmpty() { return users.isEmpty(); }

    public MemoryUserDAO() {
        users = new HashMap<>();
    }

    public void removeAllUsers() {
        users.clear();
    }

}
