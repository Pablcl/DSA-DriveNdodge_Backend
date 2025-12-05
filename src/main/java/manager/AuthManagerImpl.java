package manager;


import db.orm.SessionImpl;
import db.orm.dao.IUsuarioDAO;
import db.orm.dao.UsuarioDAOImpl;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.util.List;

public class AuthManagerImpl implements AuthManager {
    private static final Logger LOGGER = Logger.getLogger(AuthManagerImpl.class);

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
    public void register(Usuario usuario) {
        LOGGER.info(" Inicio login: username: " + usuario.getUsername()+ " password: " + usuario.getPassword()+" nombre: " + usuario.getNombre() +" apellido: " + usuario.getApellido()+" gmail: " + usuario.getGmail()+" fechaNacimiento: " + usuario.getFechaNacimiento());
        Usuario existent = userDAO.getUsuarioByEmail(usuario.getGmail());
        if (existent != null) {
            LOGGER.error("Intento de registro fallido: El usuario con correo ya existe: " + usuario.getGmail());
            throw new RuntimeException("El usuario ya existe");
        }
        usuario.setId(0); //PER AUTOINCREMENT EN BASE DADES
        userDAO.addUsuario(usuario);
        LOGGER.info("Se ha registrado un nuevo usuario: " + usuario.getUsername());

    }

    @Override
    public Usuario login(Usuario usuario) {
        LOGGER.info(" Inicio login: username: " + usuario.getUsername()+ " password: " + usuario.getPassword());
        Usuario usuarioExistent = userDAO.getUsuarioByUsername(usuario.getUsername());
        if (usuarioExistent == null || !usuarioExistent.getPassword().equals(usuario.getPassword())) {
            LOGGER.error("Intento de login fallido para el usuario: " + usuario);
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        LOGGER.info("Inicio de sesión exitoso: Usuario " + usuario);
        return usuarioExistent;
    }

    @Override
    public List<Usuario> getRegisteredUsers() {
        return userDAO.getUsuarios();
    }
}
