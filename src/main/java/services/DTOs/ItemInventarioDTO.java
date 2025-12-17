package services.DTOs;

import db.orm.model.Item;

public class ItemInventarioDTO {
    private int id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String imagen;
    private int cantidad;

    public ItemInventarioDTO() {}

    public ItemInventarioDTO(Item item, int cantidad) {
        this.id = item.getId();
        this.nombre = item.getNombre();
        this.descripcion = item.getDescripcion();
        this.precio = item.getPrecio();
        this.imagen = item.getImagen();
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}