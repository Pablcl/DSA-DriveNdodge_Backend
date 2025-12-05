package manager;


import db.orm.model.Usuario;

import java.util.List;

public interface AuthManager {
    void register(Usuario usr);
    Usuario login(Usuario usr);
    List<Usuario> getRegisteredUsers();
}
