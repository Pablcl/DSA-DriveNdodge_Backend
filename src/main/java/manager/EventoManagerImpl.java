package manager;

import db.orm.dao.EventoDAO;
import db.orm.dao.EventoDAOImpl;
import db.orm.dao.UsuarioDAOImpl;
import db.orm.model.Evento;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.util.List;

public class EventoManagerImpl implements EventoManager {
    private static final Logger LOGGER = Logger.getLogger(EventoManagerImpl.class);
    private static EventoManagerImpl instance;
    private EventoDAO eventoDAO;
    private UsuarioDAOImpl usuarioDAO;

    private EventoManagerImpl() {
        this.eventoDAO = EventoDAOImpl.getInstance();
        this.usuarioDAO = UsuarioDAOImpl.getInstance();
    }

    public static EventoManagerImpl getInstance() {
        if (instance == null) instance = new EventoManagerImpl();
        return instance;
    }

    @Override
    public List<Evento> getListaEventos() {
        LOGGER.info("Solicitando lista de eventos");
        return this.eventoDAO.getEventos();
    }

    @Override
    public void inscribirse(String username, int eventoId) {
        LOGGER.info("Usuario " + username + " intenta inscribirse al evento " + eventoId);

        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        if (u == null) {
            LOGGER.error("Usuario no encontrado");
            return;
        }

        Evento e = this.eventoDAO.getEventoById(eventoId);
        if (e == null) {
            LOGGER.error("Evento no encontrado");
            return;
        }

        List<Usuario> participantes = this.eventoDAO.getUsuariosInscritos(eventoId);
        if (participantes != null) {
            for (Usuario participante : participantes) {
                if (participante.getId() == u.getId()) {
                    LOGGER.error("El usuario " + username + " YA está inscrito en el evento " + eventoId);
                    return;
                }
            }
        }

        this.eventoDAO.inscribirUsuario(u.getId(), eventoId);
        LOGGER.info("Inscripción realizada con éxito");
    }

    @Override
    public List<Usuario> getParticipantes(int eventoId) {
        LOGGER.info("Obteniendo participantes del evento " + eventoId);
        return this.eventoDAO.getUsuariosInscritos(eventoId);
    }
}