package manager;

import database.models.Usuario;
import java.util.List;

public interface AuthManager {
    void register(String username, String password);
    Usuario login(String username, String password);
    List<Usuario> getRegisteredUsers();
}
