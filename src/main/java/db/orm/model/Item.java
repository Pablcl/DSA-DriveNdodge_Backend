package db.orm.model;

public class Item {
    private int ID;
    private String nombre;
    private String descripcion;
    private int precio;
    private String imagen;
    public Item() {
    }

    public Item(int ID, String nombre, String descripcion, int precio,  String imagen) {
        this.ID = ID;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public String getImagen() { return  imagen;}
    public void setImagen(String imatge) {
        this.imagen = imagen;
    }
    @Override
    public String toString() {
        return "Item{" +
                "id=" + ID +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                '}';
    }
}