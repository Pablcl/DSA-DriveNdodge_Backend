package db.orm.model;

public class Inventario {
    private int ID;
    private int usuarioId;
    private int itemId;
    private int cantidad;

    public Inventario() {}
    public Inventario(int usuarioId, int itemId, int cantidad) {
        this.usuarioId = usuarioId;
        this.itemId = itemId;
        this.cantidad = cantidad;
    }
    public int getId() {return ID;}
    public void setId(int ID) {this.ID = ID;}
    public int getUsuarioId() {return usuarioId;}
    public void setUsuarioId(int usuarioId) {this.usuarioId = usuarioId;}
    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


}
