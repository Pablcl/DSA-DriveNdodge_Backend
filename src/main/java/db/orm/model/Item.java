package db.orm.model;

public class Item {
    private Integer id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String imagen;
    public Item() {
    }

    public Item(int id, String nombre, String descripcion, int precio,  String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                '}';
    }
}