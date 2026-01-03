package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Evento;
import db.orm.model.InscripcionEvento;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventoDAOImpl implements EventoDAO {
    private static final Logger logger = Logger.getLogger(EventoDAOImpl.class);
    private static EventoDAOImpl instance;

    private EventoDAOImpl() {
        logger.debug("Instancia de EventoDAOImpl creada");
    }

    public static EventoDAOImpl getInstance() {
        if (instance == null) {
            logger.info("Creando instancia Singleton de EventoDAOImpl");
            instance = new EventoDAOImpl();
        }
        return instance;
    }

    @Override
    public List<Evento> getEventos() {
        logger.info("Obteniendo lista de todos los eventos");
        Session session = null;
        List<Evento> eventos = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            eventos = (List<Evento>)(List<?>) session.findAll(Evento.class);
            logger.info("Se obtuvieron " + eventos.size() + " eventos");
        } catch (Exception e) {
            logger.error("Error al obtener la lista de eventos: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return eventos;
    }

    @Override
    public Evento getEventoById(int id) {
        logger.info("Obteniendo evento con ID: " + id);
        Session session = null;
        Evento evento = null;
        try {
            session = FactorySession.openSession();
            evento = (Evento) session.get(Evento.class, id);
            if (evento != null) {
                logger.info("Evento encontrado: " + evento.getNombre());
            } else {
                logger.warn("No se encontró evento con ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error al obtener evento con ID " + id + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return evento;
    }

    @Override
    public void inscribirUsuario(int usuarioId, int eventoId) {
        logger.info("Inscribiendo usuario " + usuarioId + " en evento " + eventoId);
        Session session = null;
        try {
            session = FactorySession.openSession();
            InscripcionEvento inscripcion = new InscripcionEvento(usuarioId, eventoId);
            logger.debug("Creada inscripción: usuarioId=" + usuarioId + ", eventoId=" + eventoId);
            session.save(inscripcion);
            logger.info("Usuario " + usuarioId + " inscrito correctamente en evento " + eventoId);
        } catch (Exception e) {
            logger.error("Error al inscribir usuario " + usuarioId + " en evento " + eventoId + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Usuario> getUsuariosInscritos(int eventoId) {
        logger.info("Obteniendo usuarios inscritos en evento " + eventoId);
        Session session = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            String sql = "SELECT u.* FROM Usuario u JOIN InscripcionEvento i ON u.ID = i.usuarioId WHERE i.eventoId = ?";
            //logger.debug("Query SQL: " + sql);
            HashMap<String, Object> params = new HashMap<>();
            params.put("eventoId", eventoId);
            usuarios = (List<Usuario>)(List<?>) session.query(Usuario.class, sql, params);
            logger.info("Se obtuvieron " + usuarios.size() + " usuarios inscritos en evento " + eventoId);
        } catch (Exception e) {
            logger.error("Error al obtener usuarios inscritos en evento " + eventoId + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usuarios;
    }
}