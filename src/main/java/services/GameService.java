package services;

import manager.GameManager;
import manager.GameManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import services.DTOs.InventarioRequest;
import services.DTOs.PartidaRequest;
import services.DTOs.MessageResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/game", description = "Servicios de gestión de Partidas")
@Path("/game")
public class GameService {

    private final GameManager manager;

    public GameService() {
        this.manager = GameManagerImpl.getInstance();
    }

    @POST
    @Path("/partida")
    @ApiOperation(value = "Guardar resultado de partida", notes = "Actualiza puntuación y suma monedas")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Partida guardada correctamente"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 500, message = "Error interno")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarPartida(PartidaRequest request) {
        // Validación básica
        if (request.getUsername() == null) {
            return Response.status(400).entity(new MessageResponse("Falta username")).build();
        }

        try {
            // Llamamos al Manager para que haga la magia
            boolean exito = this.manager.procesarPartida(request.getUsername(), request.getPuntos(), request.getMonedas());

            if (exito) {
                return Response.status(201).entity(new MessageResponse("Partida guardada")).build();
            } else {
                return Response.status(404).entity(new MessageResponse("Usuario no encontrado")).build();
            }
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    @POST
    @Path("/inventario")
    @ApiOperation(value = "Actualizar inventario tras partida", notes = "Resta los objetos usados")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarInventario(InventarioRequest request) {
        if (request.getUsername() == null) return Response.status(400).build();

        try {
            this.manager.actualizarObjetosJugador(
                    request.getUsername(),
                    request.getMagnet(),
                    request.getShield(),
                    request.getDoubler()
            );

            return Response.status(201).entity(new MessageResponse("Inventario actualizado")).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

}