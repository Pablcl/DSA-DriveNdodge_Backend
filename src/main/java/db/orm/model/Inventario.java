package db.orm.model;

public class Inventario {
    private int ID;
    private int usuarioId;
    private int itemId;

    public Inventario() {}
    public Inventario(int usuarioId, int itemId) {
        this.usuarioId = usuarioId;
        this.itemId = itemId;
    }
    public int getId() {return ID;}
    public void setId(int ID) {this.ID = ID;}
    public int getUsuarioId() {return usuarioId;}
    public void setUsuarioId(int usuarioId) {this.usuarioId = usuarioId;}
}
