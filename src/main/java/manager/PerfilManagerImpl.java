package manager;
import db.orm.dao.IUsuarioDAO;
import db.orm.dao.UsuarioDAOImpl;

import db.orm.model.Usuario;
import db.orm.dao.*;
import org.apache.log4j.Logger;

public class PerfilManagerImpl implements PerfilManager {

    private static final Logger LOGGER = Logger.getLogger(PerfilManagerImpl.class);

    private static PerfilManagerImpl instance;
    private final IUsuarioDAO usuarioDAO;

    private PerfilManagerImpl() {
        this.usuarioDAO = UsuarioDAOImpl.getInstance();
    }

    public static PerfilManagerImpl getInstance() {
        if (instance == null) {
            instance = new PerfilManagerImpl();
            LOGGER.info("Instancia de PerfilManagerImpl creada");
        }
        return instance;
    }

    @Override
    public Usuario getPerfil(String username){
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);

        if (u == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        LOGGER.info("Obtenint perfil de: " + u.getEmail());
        return u;
    }

    public Usuario updatePerfil(String username, String nombre, String apellido, String email, String fechaNacimiento) {
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        if (u != null) {
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setEmail(email);
            u.setFechaNacimiento(fechaNacimiento);

            this.usuarioDAO.updateUsuario(u);
        }
        return u;
    }

}
