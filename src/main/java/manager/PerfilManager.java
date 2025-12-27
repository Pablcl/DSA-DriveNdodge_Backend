package manager;

import db.orm.model.Usuario;

public interface PerfilManager {
    Usuario getPerfil(String username);
    public Usuario updatePerfil(String username, String nombre, String apellido, String email, String fechaNacimiento, String imagenPerfil);
}
