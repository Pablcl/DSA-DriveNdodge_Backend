package services;

import db.orm.model.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import manager.PerfilManagerImpl;
import manager.ShopManagerImpl;
import services.DTOs.MessageResponse;
import services.DTOs.UsuarioPerfilDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/perfil", description = "Servicios de perfil de usuario")
@Path("/perfil")
public class PerfilService {

    private final PerfilManagerImpl perfilManager;

    public PerfilService() {
        this.perfilManager = PerfilManagerImpl.getInstance();
    }

    @GET
    @Path("/{username}")
    @ApiOperation(value = "Obtener perfil usuario")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Obtener perfil usuario con exito", response = UsuarioPerfilDTO.class),
            @ApiResponse(code = 404, message = "Usuario no encontrado", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Username inválido", response = MessageResponse.class)

    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerfil(@PathParam("username") String username) {

        if (username == null || username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageResponse("Username inválido"))
                    .build();
        }
        username = username.replace("\"", "").trim();

        try{
            Usuario u = perfilManager.getPerfil(username);
            UsuarioPerfilDTO dto = new UsuarioPerfilDTO(u);
            return Response.status(Response.Status.OK)
                    .entity(dto)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MessageResponse(e.getMessage()))
                    .build();
        }
    }
}
