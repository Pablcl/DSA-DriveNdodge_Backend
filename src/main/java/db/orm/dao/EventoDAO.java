package db.orm.dao;

import db.orm.model.Evento;
import db.orm.model.Usuario;
import java.util.List;

public interface EventoDAO {
    List<Evento> getEventos();
    Evento getEventoById(int id);
    void inscribirUsuario(int usuarioId, int eventoId);
    List<Usuario> getUsuariosInscritos(int eventoId);
}