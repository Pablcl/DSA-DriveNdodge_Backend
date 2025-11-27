package manager;

import database.BaseDeDatos;
import database.impl.BaseDeDatosHashMap;
import database.models.Usuario;
import org.apache.log4j.Logger;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AuthManagerImpl implements AuthManager {
    private static final Logger LOGGER = Logger.getLogger(AuthManagerImpl.class);

    private static AuthManagerImpl instance;
    private final BaseDeDatos baseDeDatos;

    private AuthManagerImpl() {
        this.baseDeDatos = BaseDeDatosHashMap.getInstance();
    }

    public static AuthManagerImpl getInstance() {
        if (instance == null) {
            instance = new AuthManagerImpl();
            LOGGER.info("Instancia de AuthManagerImpl creado");
        }
        return instance;
    }

    // ==========================
    // VALIDACIÓN DE REGISTRO
    // ==========================
    // Método interno que valida username, nombre y apellido para evitar
    // caracteres especiales en campos no-email, y valida la fecha de nacimiento.
    private void validateRegistrationData(Usuario usr) {
        if (usr == null) {
            throw new RuntimeException("Datos de usuario inválidos");
        }

        // username: solo letras y números, sin espacios ni caracteres especiales
        String regexUsername = "^[a-zA-Z0-9]+$";
        // nombre/apellido: letras, acentos y espacios
        String regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";

        if (usr.getUsername() == null || !usr.getUsername().matches(regexUsername)) {
            throw new RuntimeException("El usuario solo puede contener letras y números (sin espacios ni caracteres especiales).");
        }

        if (usr.getNombre() == null || !usr.getNombre().matches(regexNombre)) {
            throw new RuntimeException("El nombre solo puede contener letras y espacios.");
        }

        if (usr.getApellido() == null || !usr.getApellido().matches(regexNombre)) {
            throw new RuntimeException("El apellido solo puede contener letras y espacios.");
        }

        // --- VALIDACIÓN DE FECHA DE NACIMIENTO (Mínimo 16 años, Máximo 122 años) ---
        final int MIN_AGE = 16;
        final int MAX_AGE = 122;
        if (usr.getFechaNacimiento() == null) {
            throw new RuntimeException("Fecha de nacimiento obligatoria.");
        }
        try {
            LocalDate fechaNacimiento = LocalDate.parse(usr.getFechaNacimiento());
            LocalDate fechaCorteMin = LocalDate.now().minusYears(MIN_AGE); // debe haber nacido en o antes de esta fecha
            LocalDate fechaCorteMax = LocalDate.now().minusYears(MAX_AGE); // no puede ser anterior a esta fecha (demasiado mayor)

            if (fechaNacimiento.isAfter(LocalDate.now())) {
                throw new RuntimeException("La fecha de nacimiento no puede ser futura.");
            }

            if (fechaNacimiento.isAfter(fechaCorteMin)) {
                throw new RuntimeException("Debes tener al menos " + MIN_AGE + " años para registrarte.");
            }

            if (fechaNacimiento.isBefore(fechaCorteMax)) {
                throw new RuntimeException("No eres tan viejo, no intentes registrarte con más de " + MAX_AGE + " años.");
            }
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de fecha de nacimiento inválido (debe ser AAAA-MM-DD).");
        }
        // No validamos password ni email aquí porque tienen sus propias reglas.
    }

    @Override
    public void register(Usuario usr) {
        // Validación preventiva: evita que clientes puedan registrar usernames con caracteres especiales
        validateRegistrationData(usr);

        LOGGER.info(" Inicio registro: username: " + usr.getUsername()+ " password: " + usr.getPassword()+" nombre: " + usr.getNombre() +" apellido: " + usr.getApellido()+" gmail: " + usr.getEmail()+" fechaNacimiento: " + usr.getFechaNacimiento());
        if (baseDeDatos.getUsuario(usr.getUsername()) != null) {
            LOGGER.error("Intento de registro fallido: El usuario ya existe: " + usr);
            throw new RuntimeException("El usuario ya existe");
        }
        LOGGER.info("Se ha registrado un nuevo usuario: " + usr);
        baseDeDatos.addUsuario(usr);
    }

    @Override
    public Usuario login(Usuario usr) {
        LOGGER.info(" Inicio login: username: " + usr.getUsername()+ " password: " + usr.getPassword());
        Usuario usuario = baseDeDatos.getUsuario(usr.getUsername());
        if (usuario == null || !usuario.getPassword().equals(usr.getPassword())) {
            LOGGER.error("Intento de login fallido para el usuario: " + usr);
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        LOGGER.info("Inicio de sesión exitoso: Usuario " + usr);
        return usuario;
    }

    @Override
    public List<Usuario> getRegisteredUsers() {
        return baseDeDatos.getUsuarios();
    }
}
