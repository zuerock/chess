package dataAccess.interfaces;

import model.UserData;

public interface UserDao {
    public void createUser(UserData data);
    public UserData getUser(UserData data);
    public void clear();
}
