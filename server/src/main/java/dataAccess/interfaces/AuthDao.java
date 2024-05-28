package dataAccess.interfaces;

import model.AuthData;

public interface AuthDao {
    public void createAuth(AuthData data);
    public AuthData getUser(AuthData data);
    public void deleteAuth(AuthData data);
    public void clear();
}
