package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData newUser);
    void removeUser(int index);
    model.UserData getUser(int index);
    int getSize();
    void clearUserList();
}
