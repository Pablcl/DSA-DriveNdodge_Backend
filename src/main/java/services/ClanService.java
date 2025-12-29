package services;

import db.orm.model.Clan;
import db.orm.model.Usuario;
import manager.ClanManager;
import manager.ClanManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import services.DTOs.ClanRankingDTO;
import services.DTOs.MessageResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response crearClan(Clan clan) {
        if (clan.getNombre() == null || clan.getDescripcion() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new MessageResponse("Datos incompletos")).build();

        Clan nuevo = this.manager.crearClan(clan.getNombre(), clan.getDescripcion());
        if (nuevo != null)
            return Response.status(Response.Status.CREATED).entity(nuevo).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageResponse("Error al crear el clan")).build();
    }

    @PUT
    @Path("/join/{clanName}")
    @ApiOperation(value = "Unirse a un clan", notes = "Añade al usuario especificado al clan indicado.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario unido correctamente"),
            @ApiResponse(code = 404, message = "Clan o Usuario no encontrado")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unirseClan(@PathParam("clanName") String clanName, Usuario u) {
        this.manager.unirseClan(u.getUsername(), clanName);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{clanName}/members")
    @ApiOperation(value = "Obtener miembros del clan", notes = "Lista los usuarios que pertenecen a un clan.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista obtenida", response = Usuario.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Clan no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMiembros(@PathParam("clanName") String clanName) {
        List<Usuario> miembros = this.manager.getMiembros(clanName);
        if (miembros != null) {
            GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(miembros) {};
            return Response.status(Response.Status.OK).entity(entity).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}