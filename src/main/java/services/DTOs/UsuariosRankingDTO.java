package services.DTOs;

import db.orm.model.Usuario;

public class UsuariosRankingDTO {

    private String username;
    private String nombre;
    private Integer mejorPuntuacion;
    private String imagenPerfil;

    public UsuariosRankingDTO() {}

    public UsuariosRankingDTO(Usuario u) {
        this.username = u.getUsername();
        this.nombre = u.getNombre();
        this.mejorPuntuacion = u.getMejorPuntuacion();
        this.imagenPerfil = u.getImagenPerfil();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getMejorPuntuacion() { return mejorPuntuacion; }
    public void setMejorPuntuacion(Integer mejorPuntuacion) { this.mejorPuntuacion = mejorPuntuacion; }

    public String getImagenPerfil() { return imagenPerfil; }
    public void setImagenPerfil(String imagenPerfil) { this.imagenPerfil = imagenPerfil;}

}