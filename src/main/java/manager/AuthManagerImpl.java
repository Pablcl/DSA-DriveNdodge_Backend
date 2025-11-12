package manager;

import database.BaseDeDatos;
import database.impl.BaseDeDatosHashMap;
import database.models.Usuario;

import java.util.List;

public class AuthManagerImpl implements AuthManager {

    private static AuthManagerImpl instance;
    private final BaseDeDatos baseDeDatos;

    private AuthManagerImpl() {
        this.baseDeDatos = BaseDeDatosHashMap.getInstance();
    }

    public static AuthManagerImpl getInstance() {
        if (instance == null) {
            instance = new AuthManagerImpl();
        }
        return instance;
    }

    @Override
    public void register(String username, String password) {
        if (baseDeDatos.getUsuario(username) != null) {
            throw new RuntimeException("El usuario ya existe");
        }
        // ¡OJO! Loggear contraseñas es inseguro. Solo para depuración.
        System.out.println("Nuevo registro: Usuario '" + username + "', Contraseña '" + password + "' se ha registrado.");
        baseDeDatos.addUsuario(new Usuario(username, password));
    }

    @Override
    public Usuario login(String username, String password) {
        Usuario usuario = baseDeDatos.getUsuario(username);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            System.out.println("Intento de login fallido para el usuario: '" + username + "'");
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        System.out.println("Inicio de sesión exitoso: Usuario '" + username + "', Contraseña '" + password + "'");
        return usuario;
    }

    @Override
    public List<Usuario> getRegisteredUsers() {
        return baseDeDatos.getUsuarios();
    }
}
