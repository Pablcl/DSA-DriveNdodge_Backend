package services;

import database.models.Usuario;
import manager.AuthManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Api(value = "/auth", description = "Servicios de autenticación de usuarios")
@Path("/auth")
public class AuthManagerService {

    private final AuthManagerImpl am;

    public AuthManagerService() {
        this.am = AuthManagerImpl.getInstance();
    }

    @POST
    @Path("/register")
    @ApiOperation(
            value = "Registrar un nuevo usuario",
            notes = "Crea un nuevo usuario con nombre y contraseña."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario registrado correctamente", response = Usuario.class),
            @ApiResponse(code = 400, message = "El usuario ya existe", response = String.class)
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Usuario usuario) {
        try {
            am.register(usuario);
            return Response.status(Response.Status.CREATED)
                    .entity(usuario)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    @ApiOperation(
            value = "Iniciar sesión",
            notes = "Verifica las credenciales del usuario y devuelve sus datos si son correctas."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Inicio de sesión exitoso", response = Usuario.class),
            @ApiResponse(code = 401, message = "Usuario o contraseña incorrectos", response = String.class)
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Usuario usuario) {
        try {
            Usuario u = am.login(usuario);
            return Response.status(Response.Status.OK)
                    .entity(u)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/users")
    @ApiOperation(
            value = "Obtener todos los usuarios registrados",
            notes = "Devuelve una lista de todos los usuarios en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de usuarios obtenida correctamente", response = Usuario.class, responseContainer = "List"),
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<Usuario> users = this.am.getRegisteredUsers();
        return Response.status(Response.Status.OK)
                .entity(users)
                .build();
    }
}
