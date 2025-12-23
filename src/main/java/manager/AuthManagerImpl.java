package manager;

import db.orm.dao.IUsuarioDAO;
import db.orm.dao.UsuarioDAOImpl;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

public class AuthManagerImpl implements AuthManager {
    private static final Logger LOGGER = Logger.getLogger(AuthManagerImpl.class);

    // Patrón de email simple para validación
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    private static AuthManagerImpl instance;
    private IUsuarioDAO userDAO;

    private AuthManagerImpl() {
        this.userDAO = UsuarioDAOImpl.getInstance();
    }

    public static AuthManagerImpl getInstance() {
        if (instance == null) {
            instance = new AuthManagerImpl();
            LOGGER.info("Instancia de AuthManagerImpl creada");
        }
        return instance;
    }

    // ==========================
    // VALIDACIÓN DE REGISTRO
    // ==========================
    private void validateRegistrationData(Usuario usr) {
        if (usr == null) {
            throw new RuntimeException("Datos de usuario inválidos");
        }

        // 1. Validar Email (ahora es lo primero y es obligatorio)
        if (usr.getEmail() == null || usr.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(usr.getEmail()).matches()) {
            throw new RuntimeException("El formato del email no es válido.");
        }

        // 2. Validar resto de campos
        String regexUsername = "^[a-zA-Z0-9]+$";
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

        final int MIN_AGE = 16;
        final int MAX_AGE = 122;
        if (usr.getFechaNacimiento() == null) {
            throw new RuntimeException("Fecha de nacimiento obligatoria.");
        }
        try {
            LocalDate fechaNacimiento = LocalDate.parse(usr.getFechaNacimiento());
            LocalDate fechaCorteMin = LocalDate.now().minusYears(MIN_AGE);
            LocalDate fechaCorteMax = LocalDate.now().minusYears(MAX_AGE);

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
    }

    @Override
    public void register(Usuario usuario) {
        validateRegistrationData(usuario);
        LOGGER.info("Usuario validado correctamente: " + usuario.getUsername());

        // Comprobar si el email ya existe
        Usuario existentByEmail = userDAO.getUsuarioByEmail(usuario.getEmail());
        if (existentByEmail != null) {
            LOGGER.error("Intento de registro fallido: El email ya está en uso: " + usuario.getEmail());
            throw new RuntimeException("El email ya está registrado. Por favor, utiliza otro.");
        }

        // Comprobar si el nombre de usuario ya existe
        Usuario existentByUsername = userDAO.getUsuarioByUsername(usuario.getUsername());
        if (existentByUsername != null) {
            LOGGER.error("Intento de registro fallido: El nombre de usuario ya existe: " + usuario.getUsername());
            throw new RuntimeException("El nombre de usuario ya existe. Por favor, elige otro.");
        }

        // Si todo es correcto, proceder con el registro
        LOGGER.info("Inicio de registro para el nuevo usuario: " + usuario.getUsername());
        usuario.setId(0); // Para autoincrement en la base de datos
        userDAO.addUsuario(usuario);
        LOGGER.info("Se ha registrado un nuevo usuario: " + usuario.getUsername());
    }

    @Override
    public Usuario login(Usuario usuario) {
        // --- LOG DE DEPURACIÓN ---
        LOGGER.info("Intento de login para username: '" + usuario.getUsername() + "' con password: '" + usuario.getPassword() + "'");

        Usuario usuarioExistent = userDAO.getUsuarioByCred(usuario.getUsername(), usuario.getPassword());

        if (usuarioExistent != null) {
            LOGGER.info("Usuario encontrado en BD: '" + usuarioExistent.getUsername() + "' con password en BD: '" + usuarioExistent.getPassword() + "'");
        } else {
            LOGGER.warn("Usuario '" + usuario.getUsername() + "' NO encontrado en la base de datos.");
        }

        if (usuarioExistent == null) {
            LOGGER.error("FALLO en la comparación de contraseñas para el usuario: " + usuario.getUsername());
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        LOGGER.info("Inicio de sesión exitoso para: " + usuario.getUsername());
        return usuarioExistent;
    }

    @Override
    public List<Usuario> getRegisteredUsers() {
        return userDAO.getUsuarios();
    }
}
