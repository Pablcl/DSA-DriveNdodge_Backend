package services;

import db.orm.model.Evento;
import db.orm.model.Usuario;
import manager.EventoManager;
import manager.EventoManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import services.DTOs.MessageResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/eventos", description = "Servicios de gestión de Eventos")
@Path("/eventos")
public class EventoService {

    private EventoManager manager;

    public EventoService() {
        this.manager = EventoManagerImpl.getInstance();
    }

    @GET
    @Path("/list")
    @ApiOperation(value = "Obtener todos los eventos", notes = "Devuelve la lista de eventos disponibles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista obtenida", response = Evento.class, responseContainer="List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventos() {
        List<Evento> eventos = this.manager.getListaEventos();
        GenericEntity<List<Evento>> entity = new GenericEntity<List<Evento>>(eventos) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @POST
    @Path("/join/{eventoId}")
    @ApiOperation(value = "Inscribirse a un evento", notes = "El usuario se apunta al evento indicado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Inscripción correcta"),
            @ApiResponse(code = 404, message = "Usuario o evento no encontrado")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inscribirse(@PathParam("eventoId") int eventoId, Usuario u) {
        if (u == null || u.getUsername() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        this.manager.inscribirse(u.getUsername(), eventoId);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{eventoId}/users")
    @ApiOperation(value = "Ver participantes", notes = "Devuelve los usuarios apuntados a un evento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista obtenida", response = Usuario.class, responseContainer="List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParticipantes(@PathParam("eventoId") int eventoId) {
        List<Usuario> users = this.manager.getParticipantes(eventoId);
        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(users) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }
}