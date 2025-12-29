package db.orm.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Usuario {
    private int ID;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNacimiento;
    private int monedas;
    private int mejorPuntuacion;
    private String imagenPerfil;
    private Integer clanId;

    public Usuario() {
        this.monedas = 1000;
        this.imagenPerfil = "avatar_default.webp";
    }

    public Usuario(int ID, String username, String password,  String nombre, String apellido, String email, String fechaNacimiento) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.mejorPuntuacion = 0;
    }
    public int getId() {return ID;}
    public void setId(int id) {this.ID = id;}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }
    public int  getMejorPuntuacion() { return mejorPuntuacion; }
    public void setMejorPuntuacion(int mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    @Override
    public String toString() {
        return "Usuario{username='" + username + "'}";
    }

    public Integer getClanId() {
        return clanId;
    }

    public void setClanId(Integer clanId) {
        this.clanId = clanId;
    }
}
