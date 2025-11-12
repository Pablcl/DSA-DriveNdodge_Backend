package database.impl;

import database.BaseDeDatos;
import database.models.Usuario;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeDatosHashMap implements BaseDeDatos {
    private static final Logger LOGGER = Logger.getLogger(BaseDeDatosHashMap.class);
    private static BaseDeDatosHashMap instance;
    private Map<String, Usuario> usuarios;

    private BaseDeDatosHashMap() {
        this.usuarios = new HashMap<>();
    }

    public static synchronized BaseDeDatosHashMap getInstance() {
        if (instance == null) {
            instance = new BaseDeDatosHashMap();
            LOGGER.info("Instancia de BaseDeDatosHashMap creada");
        }
        return instance;
    }

    @Override
    public void addUsuario(Usuario usuario) {
        LOGGER.info("Añadiendo usuario: " + usuario);
        usuarios.put(usuario.getUsername(), usuario);
    }

    @Override
    public Usuario getUsuario(String username) {
        return usuarios.get(username);
    }

    @Override
    public void removeUsuario(String username) {
        usuarios.remove(username);
    }

    @Override
    public void updateUsuario(Usuario usuario) {
        usuarios.put(usuario.getUsername(), usuario);
    }

    @Override
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios.values());
    }
}
