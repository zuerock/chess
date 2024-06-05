package dataaccess;


import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<UserData> userList = new ArrayList<>();

    public void createUser(UserData user){
        userList.add(user);
    }
    public void deleteUser(UserData user){
        userList.remove(user);
    }

    List<UserData> getUser(){return userList;}

    public void clearUserList(){
        userList.clear();
    }
}