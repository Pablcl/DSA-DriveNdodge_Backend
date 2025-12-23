package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Usuario;
import db.orm.util.QueryHelper;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDAOImpl.class);
    private static UsuarioDAOImpl instance;

    private UsuarioDAOImpl() {
    }

    public static UsuarioDAOImpl getInstance() {
        if (instance == null) {
            instance = new UsuarioDAOImpl();
        }
        return instance;
    }

    public int addUsuario(Usuario usuario) {
        Session session = null;
        int ID = 0;
        try {
            session = FactorySession.openSession();
            LOGGER.info("Iniciando operación para guardar usuario: " + usuario.getUsername());
            session.save(usuario);
            LOGGER.info("Operación save(usuario) completada con éxito para: " + usuario.getUsername());
            ID = usuario.getId(); // Asumiendo que el ID se popula después de guardar
        } catch (Exception e) {
            LOGGER.error("ERROR al guardar el usuario: " + usuario.getUsername(), e);
            // Volvemos a lanzar la excepción para que la capa superior se entere
            throw new RuntimeException("Error en la base de datos al registrar el usuario", e);
        } finally {
            if (session != null) {
                session.close();
                LOGGER.info("Sesión de base de datos cerrada para addUsuario.");
            }
        }
        return ID;
    }

    public Usuario getUsuario(int ID) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = FactorySession.openSession();
            usuario = (Usuario) session.get(Usuario.class, ID);
        } catch (Exception e) {
            LOGGER.error("ERROR al obtener usuario por ID: " + ID, e);
        } finally {
            if (session != null) session.close();
        }
        return usuario;
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        Session session = FactorySession.openSession();
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("email", email);
            List<Object> result = session.findAll(Usuario.class, params);
            return result.isEmpty() ? null : (Usuario) result.get(0);
        } catch (Exception e) {
            LOGGER.error("ERROR al obtener usuario por email: " + email, e);
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Usuario getUsuarioByUsername(String username) {
        Session session = FactorySession.openSession();
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);
            List<Object> result = session.findAll(Usuario.class, params);
            return result.isEmpty() ? null : (Usuario) result.get(0);
        } catch (Exception e) {
            LOGGER.error("ERROR al obtener usuario por username: " + username, e);
            return null;
        } finally {
            session.close();
        }
    }

    public void updateUsuario(Usuario usuario) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(usuario);
        } catch (Exception e) {
            LOGGER.error("ERROR al actualizar usuario: " + usuario.getUsername(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Usuario> getUsuariosRanking() {
        Session session = FactorySession.openSession();
        List<Usuario> usersRanking = null;
        try {
            String sql = QueryHelper.ordenateQuery(Usuario.class, "mejorPuntuacion");
            usersRanking = session.query(Usuario.class, sql, null);
        } catch (Exception e) {
            LOGGER.error("ERROR al obtener el ranking de usuarios", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usersRanking;
    }

    public void deleteUsuario(int ID) {
        // Implementación pendiente
    }

    public List<Usuario> getUsuarios() {
        // Implementación pendiente
        return null;
    }

    public List<Usuario> getUsuarioByDept(int ID) {
        // Implementación pendiente
        return null;
    }
    public Usuario getUsuarioByCred(String username, String password) {
        Session session = FactorySession.openSession();
        Usuario usuario = null;
        try {
            String sql = QueryHelper.createSelectByCred(Usuario.class);

            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            List<Object> result = session.query(Usuario.class, sql, params);

            if (!result.isEmpty()) {
                usuario = (Usuario) result.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("ERROR en login (getUsuarioByCredentials) para: " + username, e);
        } finally {
            session.close();
        }
        return usuario;
    }
}
