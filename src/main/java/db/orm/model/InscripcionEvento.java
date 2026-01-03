package db.orm.model;

public class InscripcionEvento {
    private int ID;
    private int usuarioId;
    private int eventoId;

    public InscripcionEvento() {
    }

    public InscripcionEvento(int usuarioId, int eventoId) {
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }
}
