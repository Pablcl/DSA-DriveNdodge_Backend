package services.DTOs;

public class UsuarioClanDTO {
    private String username;
    private String imagenPerfil;

    public UsuarioClanDTO() {}

    public UsuarioClanDTO(String username, String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
        this.username = username;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getImagenPerfil() {return imagenPerfil;}
    public void setImagenPerfil(String imagenPerfil) {this.imagenPerfil = imagenPerfil;}
}
