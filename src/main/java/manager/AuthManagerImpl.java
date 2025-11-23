package manager;


import db.orm.SessionImpl;
import db.orm.dao.IUsuarioDAO;
import db.orm.dao.UsuarioDAOImpl;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.util.List;

public class AuthManagerImpl implements AuthManager {
    private static final Logger LOGGER = Logger.getLogger(AuthManagerImpl.class);

    private static AuthManagerImpl instance;
    private IUsuarioDAO userDAO;

    private AuthManagerImpl() {
        this.userDAO = UsuarioDAOImpl.getInstance();
    }

    public static AuthManagerImpl getInstance() {
        if (instance == null) {
            instance = new AuthManagerImpl();
            LOGGER.info("Instancia de AuthManagerImpl creada");
        }
        return instance;
    }

    @Override
    public void register(Usuario usuario) {
        LOGGER.info(" Inicio login: username: " + usuario.getUsername()+ " password: " + usuario.getPassword()+" nombre: " + usuario.getNombre() +" apellido: " + usuario.getApellido()+" gmail: " + usuario.getGmail()+" fechaNacimiento: " + usuario.getFechaNacimiento());
        Usuario existent = userDAO.getUsuarioByEmail(usuario.getGmail());
        if (existent != null) {
            LOGGER.error("Intento de registro fallido: El usuario con correo ya existe: " + usuario.getGmail());
            throw new RuntimeException("El usuario ya existe");
        }
        usuario.setId(0); //PER AUTOINCREMENT EN BASE DADES
        userDAO.addUsuario(usuario);
        LOGGER.info("Se ha registrado un nuevo usuario: " + usuario.getUsername());

    }

    @Override
    public Usuario login(Usuario usuario) {
        LOGGER.info(" Inicio login: username: " + usuario.getUsername()+ " password: " + usuario.getPassword());
        Usuario usuarioExistent = userDAO.getUsuarioByUsername(usuario.getUsername());
        if (usuarioExistent == null || !usuarioExistent.getPassword().equals(usuario.getPassword())) {
            LOGGER.error("Intento de login fallido para el usuario: " + usuario);
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        LOGGER.info("Inicio de sesión exitoso: Usuario " + usuario);
        return usuarioExistent;
    }

    @Override
    public List<Usuario> getRegisteredUsers() {
        return userDAO.getUsuarios();
    }
}
