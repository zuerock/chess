package dataAccess.memory;

import dataAccess.interfaces.UserDao;
import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class MemoryUserDao implements UserDao {
    private static MemoryUserDao instance;
    private Set<UserData> user = new HashSet<>();

    private MemoryUserDao() {}

    public static MemoryUserDao getInstance() {
        if (instance == null) {
            instance = new MemoryUserDao();
        }
        return instance;
    }

    @Override
    public void createUser(UserData data) {
        user.add(data);
    }

    @Override
    public UserData getUser(UserData data) {
        for (UserData u : user) {
            if(u.username().equals(data.username())) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        user = new HashSet<>();
    }
}
