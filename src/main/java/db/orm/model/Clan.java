package db.orm.model;

public class Clan {
    private int ID;
    private String nombre;
    private String descripcion;
    private String imagen;

    public Clan() {}

    public Clan(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = "clan_default.png";
    }

    public int getId() { return ID; }
    public void setId(int id) { this.ID = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}