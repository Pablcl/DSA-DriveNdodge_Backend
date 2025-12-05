package db.orm.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Usuario {
    private int ID;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String gmail;
    private String fechaNacimiento;
    private int monedas;
    private int mejorPuntuacion;
    public Usuario() {

    }

    public Usuario(int ID, String username, String password,  String nombre, String apellido, String gmail, String fechaNacimiento) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.gmail = gmail;
        this.fechaNacimiento = fechaNacimiento;
        this.monedas = this.monedas;
        this.mejorPuntuacion = 0;
    }
    public int getId() {return ID;}
    public void setId(int id) {this.ID = ID;}
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

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
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
    @Override
    public String toString() {
        return "Usuario{username='" + username + "'}";
    }

}
