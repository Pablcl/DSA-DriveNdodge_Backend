package manager;

import db.orm.model.Usuario;

public interface PerfilManager {
    Usuario getPerfil(String username);
}
