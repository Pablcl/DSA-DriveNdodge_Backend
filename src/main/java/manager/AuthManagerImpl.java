package manager;

import database.BaseDeDatos;
import database.impl.BaseDeDatosHashMap;
import database.models.Usuario;
import org.apache.log4j.Logger;

import java.util.List;

public class AuthManagerImpl implements AuthManager {
    private static final Logger LOGGER = Logger.getLogger(AuthManagerImpl.class);

    private static AuthManagerImpl instance;
    private final BaseDeDatos baseDeDatos;

    private AuthManagerImpl() {
        this.baseDeDatos = BaseDeDatosHashMap.getInstance();
    }

    public static AuthManagerImpl getInstance() {
        if (instance == null) {
            instance = new AuthManagerImpl();
            LOGGER.info("Instancia de AuthManagerImpl creada");
        }
        return instance;
    }

    @Override
    public void register(Usuario usr) {
        if (baseDeDatos.getUsuario(usr.getUsername()) != null) {
            LOGGER.error("Intento de registro fallido: El usuario ya existe: " + usr);
            throw new RuntimeException("El usuario ya existe");
        }
        LOGGER.info("Se ha registrado un nuevo usuario: " + usr);
        baseDeDatos.addUsuario(usr);
    }

    @Override
    public Usuario login(Usuario usr) {
        Usuario usuario = baseDeDatos.getUsuario(usr.getUsername());
        if (usuario == null || !usuario.getPassword().equals(usr.getPassword())) {
            LOGGER.error("Intento de login fallido para el usuario: " + usr);
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        LOGGER.info("Inicio de sesión exitoso: Usuario " + usr);
        return usuario;
    }

    @Override
    public List<Usuario> getRegisteredUsers() {
        return baseDeDatos.getUsuarios();
    }
}
