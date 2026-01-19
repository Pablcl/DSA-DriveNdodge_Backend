package manager;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.dao.InventarioDAO;
import db.orm.dao.InventarioDAOImpl;
import db.orm.model.Inventario;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;

public class GameManagerImpl implements GameManager {
    private static final Logger logger = Logger.getLogger(GameManagerImpl.class);
    private static GameManagerImpl instance;

    private GameManagerImpl() { }

    public static GameManagerImpl getInstance() {
        if (instance == null) {
            instance = new GameManagerImpl();
        }
        return instance;
    }

    @Override
    public boolean procesarPartida(String username, int puntosNuevos, int monedasGanadas) {
        Session session = null;
        try {
            session = FactorySession.openSession();

            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);

            List<Object> result = session.findAll(Usuario.class, params);

            if (result.isEmpty()) {
                logger.warn("Usuario no encontrado al guardar partida: " + username);
                return false;
            }

            Usuario u = (Usuario) result.get(0);

            int monedasActuales = u.getMonedas();
            u.setMonedas(monedasGanadas);

            if (puntosNuevos > u.getMejorPuntuacion()) {
                logger.info("¡Nuevo récord para " + username + "! " + puntosNuevos + " pts.");
                u.setMejorPuntuacion(puntosNuevos);
            }

            session.update(u);

            logger.info("Partida guardada. User: " + username + " | +Monedas: " + monedasGanadas);
            return true;

        } catch (Exception e) {
            logger.error("Error procesando partida", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
    @Override
    public void actualizarObjetosJugador(String username, int magnetUnity, int shieldUnity, int doublerUnity) {
        Session session = null;
        try {
            session = FactorySession.openSession();

            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);
            List<Object> users = session.findAll(Usuario.class, params);

            if (users.isEmpty()) {
                logger.error("No se encontró usuario para actualizar inventario: " + username);
                return;
            }
            Usuario u = (Usuario) users.get(0);
            int usuarioId = u.getId();

            InventarioDAO inventarioDAO = InventarioDAOImpl.getInstance();
            List<Inventario> misItems = inventarioDAO.getInventario(usuarioId);

            boolean tieneMagnet = false;
            boolean tieneShield = false;
            boolean tieneDoubler = false;

            for (Inventario inv : misItems) {
                if (inv.getItemId() == 1) {
                    inv.setCantidad(magnetUnity);
                    inventarioDAO.updateInventario(inv);
                    tieneMagnet = true;
                }
                else if (inv.getItemId() == 2) {
                    inv.setCantidad(shieldUnity);
                    inventarioDAO.updateInventario(inv);
                    tieneShield = true;
                }
                else if (inv.getItemId() == 3) {
                    inv.setCantidad(doublerUnity);
                    inventarioDAO.updateInventario(inv);
                    tieneDoubler = true;
                }
            }

            if (!tieneMagnet && magnetUnity > 0) {
                inventarioDAO.addInventario(new Inventario(usuarioId, 1, magnetUnity));
            }
            if (!tieneShield && shieldUnity > 0) {
                inventarioDAO.addInventario(new Inventario(usuarioId, 2, shieldUnity));
            }
            if (!tieneDoubler && doublerUnity > 0) {
                inventarioDAO.addInventario(new Inventario(usuarioId, 3, doublerUnity));
            }
            logger.info("Inventario actualizado para " + username);

        } catch (Exception e) {
            logger.error("Error actualizando inventario", e);
        } finally {
            if (session != null) session.close();
        }
    }
    public boolean actualizarPuntuacion(String username, int puntosNuevos) {
        Session session = null;
        try {
            session = FactorySession.openSession();

            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);
            List<Object> result = session.findAll(Usuario.class, params);

            if (result.isEmpty()) return false;
            Usuario u = (Usuario) result.get(0);

            if (puntosNuevos > u.getMejorPuntuacion()) {
                u.setMejorPuntuacion(puntosNuevos);
                session.update(u); // Guardamos solo si hay cambio
                logger.info("¡Nuevo Récord para " + username + ": " + puntosNuevos + "!");
                return true;
            }
            return false;

        } catch (Exception e) {
            logger.error("Error al actualizar puntuación", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    public boolean actualizarMonedas(String username, int monedasTotales) {
        Session session = null;
        try {
            session = FactorySession.openSession();

            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);
            List<Object> result = session.findAll(Usuario.class, params);

            if (result.isEmpty()) return false;
            Usuario u = (Usuario) result.get(0);

            u.setMonedas(monedasTotales);
            session.update(u);

            logger.info("Monedas actualizadas para " + username + ": " + monedasTotales);
            return true;

        } catch (Exception e) {
            logger.error("Error al actualizar monedas", e);
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
}