package manager;

import db.orm.model.Evento;
import db.orm.model.Usuario;
import java.util.List;

public interface  EventoManager {
    List<Evento> getListaEventos();
    void inscribirse(String username, int eventoId);
    List<Usuario> getParticipantes(int eventoId);
}