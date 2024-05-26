package dataAccess.memory;

import dataAccess.interfaces.AuthDao;
import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthDao implements AuthDao {
    private static MemoryAuthDao instance;
    private Set<AuthData> auth = new HashSet<>();

    private MemoryAuthDao() {}

    public static MemoryAuthDao getInstance() {
        if (instance == null) {
            instance = new MemoryAuthDao();
        }
        return instance;
    }

    @Override
    public void createAuth(AuthData data) {
        auth.add(data);
    }

    @Override
    public AuthData getUser(AuthData data) {
        for (AuthData a : auth) {
            if(a.authtoken().equals(data.authtoken())) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) {
        for (AuthData a : auth) {
            if (a.authtoken().equals(data.authtoken())) {
                auth.remove(a);
                return;
            }
        }
    }

    @Override
    public void clear() {
        auth = new HashSet<>();
    }
}
