package services;

import db.orm.model.Clan;
import db.orm.model.Usuario;
import manager.ClanManager;
import manager.ClanManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import services.DTOs.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/clan", description = "Servicios de gestión de Clanes")
@Path("/clan")
public class ClanService {

    private final ClanManager manager;

    public ClanService() {
        this.manager = ClanManagerImpl.getInstance();
    }

    @GET
    @Path("/ranking")
    @ApiOperation(value = "Obtener Ranking de Clanes", notes = "Devuelve una lista de clanes ordenada por puntos totales.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ranking obtenido correctamente", response = ClanRankingDTO.class, responseContainer="List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRanking() {
        List<ClanRankingDTO> ranking = this.manager.getRanking();
        GenericEntity<List<ClanRankingDTO>> entity = new GenericEntity<List<ClanRankingDTO>>(ranking) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @POST
    @Path("/create")
    @ApiOperation(value = "Crear un nuevo clan", notes = "Crea un clan con nombre y descripción.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Clan creado correctamente", response = Clan.class),
            @ApiResponse(code = 500, message = "Error al crear clan (nombre duplicado o inválido)", response = MessageResponse.class)
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearClan(ClanCreationRequest request) {

        if (request.getNombre() == null || request.getDescripcion() == null || request.getUsername() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new MessageResponse("Faltan datos obligatorios")).build();
        }

        try {
            Clan nuevo = this.manager.crearClan(request.getNombre(), request.getDescripcion(), request.getImagen(), request.getUsername());

            if (nuevo != null) {
                return Response.status(Response.Status.CREATED).entity(nuevo).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageResponse("Error al crear el clan (Nombre posiblemente duplicado)")).build();
            }

        } catch (RuntimeException e) {
            return Response.status(Response.Status.CONFLICT).entity(new MessageResponse("¡Ya perteneces a un clan! Debes salir primero.")).build();
        }
    }

    @PUT
    @Path("/join/{clanName}")
    @ApiOperation(value = "Unirse a un clan")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario unido correctamente"),
            @ApiResponse(code = 400, message = "Usuario no válido"),
            @ApiResponse(code = 409, message = "El usuario ya pertenece a otro clan")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unirseClan(@PathParam("clanName") String clanName, Usuario u) {

        if (u == null || u.getUsername() == null || u.getUsername().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageResponse("Usuario no válido"))
                    .build();
        }

        try {
            manager.unirseClan(u.getUsername(), clanName);
            return Response.status(Response.Status.OK)
                    .entity(new MessageResponse("Usuario unido correctamente"))
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new MessageResponse(
                            "No puedes unirte a un clan si ya eres miembro de otro"
                    ))
                    .build();
        }
    }

    @PUT
    @Path("/leave")
    @ApiOperation(value = "Salir del clan")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario ha salido del clan"),
            @ApiResponse(code = 400, message = "Usuario no válido")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salirClan(Usuario u) {

        if (u == null || u.getUsername() == null || u.getUsername().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageResponse("Usuario no válido"))
                    .build();
        }

        manager.salirClan(u.getUsername());
        return Response.status(Response.Status.OK)
                .entity(new MessageResponse("Has salido del clan"))
                .build();
    }


    @GET
    @Path("/{clanName}/members")
    @ApiOperation(value = "Obtener miembros del clan", notes = "Lista los usuarios que pertenecen a un clan.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista obtenida", response = UsuarioClanDTO.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Clan no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMiembros(@PathParam("clanName") String clanName) {
        List<Usuario> miembros = this.manager.getMiembros(clanName);
        if (miembros == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<UsuarioClanDTO> dtoList = new ArrayList<>();
        for (Usuario u : miembros) {
            dtoList.add(new UsuarioClanDTO(u.getUsername(),u.getImagenPerfil()));
        }

        GenericEntity<List<UsuarioClanDTO>> entity =
                new GenericEntity<List<UsuarioClanDTO>>(dtoList) {};

        return Response.status(Response.Status.OK).entity(entity).build();

    }


    @GET
    @Path("/all")
    @ApiOperation(value = "Obtener lista de todos los clanes", notes = "Devuelve una lista simple con todos los clanes disponibles.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de clanes obtenida", response = Clan.class, responseContainer="List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClans() {
        List<Clan> clanes = this.manager.getAllClans();
        GenericEntity<List<Clan>> entity = new GenericEntity<List<Clan>>(clanes) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }
    @GET
    @Path("/{clanName}")
    @ApiOperation(value = "Obtener información de un clan", notes = "Devuelve los detalles (descripción, imagen) de un clan concreto.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Clan encontrado", response = Clan.class),
            @ApiResponse(code = 404, message = "Clan no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClan(@PathParam("clanName") String clanName) {
        Clan clan = this.manager.getClan(clanName);

        if (clan != null) {
            return Response.status(Response.Status.OK).entity(clan).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}