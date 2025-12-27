package services.DTOs;

import db.orm.model.Usuario;

public class UsuarioPerfilDTO {

    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private int monedas;
    private int mejorPuntuacion;
    private String fechaNacimiento;
    private String imagenPerfil;

    public UsuarioPerfilDTO() {}

    public UsuarioPerfilDTO(Usuario u) {
        this.username = u.getUsername();
        this.nombre = u.getNombre();
        this.apellido = u.getApellido();
        this.email = u.getEmail();
        this.monedas = u.getMonedas();
        this.mejorPuntuacion = u.getMejorPuntuacion();
        this.fechaNacimiento = u.getFechaNacimiento();
        this.imagenPerfil = u.getImagenPerfil();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getMonedas() { return monedas; }
    public void setMonedas(int monedas) { this.monedas = monedas; }

    public int getMejorPuntuacion() { return mejorPuntuacion; }
    public void setMejorPuntuacion(int mejorPuntuacion) { this.mejorPuntuacion = mejorPuntuacion; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getImagenPerfil() { return imagenPerfil; }
    public void setImagenPerfil(String imagenPerfil) { this.imagenPerfil = imagenPerfil; }
}
