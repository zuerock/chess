package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface AuthDAO {

    AuthData addAuth(AuthData auth);

    Collection<AuthData> authList();

    void createAuth(AuthData auth);

    void clearAuthList();
}
