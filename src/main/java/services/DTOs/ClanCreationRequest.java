package services.DTOs;

public class ClanCreationRequest {
    private String nombre;
    private String descripcion;
    private String imagen;
    private String username;

    public ClanCreationRequest() {}

    public ClanCreationRequest(String nombre, String descripcion, String imagen, String username) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getUsername() {
        return username;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}