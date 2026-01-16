package services;

import manager.ChatManager;
import manager.ChatManagerImpl;
import services.DTOs.ia.ChatIARequest;
import services.DTOs.MessageResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/chat", description = "Servicio de comunicación con IA")
@Path("/chat")
public class ChatIAService {

    private ChatManager chatManager;

    public ChatIAService() {
        this.chatManager = ChatManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "Enviar mensaje a la IA", notes = "Procesa el texto y devuelve respuesta generada")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Respuesta generada con éxito", response = MessageResponse.class),
            @ApiResponse(code = 500, message = "Error interno")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessage(ChatIARequest request) {

        String systemInstruction = "Responde SIEMPRE en Castellano y de forma breve a este mensaje: ";

        String finalPrompt = systemInstruction + request.getPrompt();
        String aiText = chatManager.askIA(finalPrompt);

        return Response.status(201).entity(new MessageResponse(aiText)).build();
    }
}