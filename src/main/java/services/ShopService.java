package services;


import db.orm.model.Item;
import db.orm.model.Usuario;

import manager.ShopManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import services.DTOs.CoinsResponse;
import services.DTOs.ItemInventario;
import services.DTOs.MessageResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Api(value = "/shop", description = "Servicios de la tienda de items")
@Path("/shop")
public class ShopService {

    private final ShopManagerImpl shopManager;

    public ShopService() {
        this.shopManager = ShopManagerImpl.getInstance();
    }

    @GET
    @Path("/items")
    @ApiOperation(value = "Obtener todos los items de la tienda")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Obtener items tienda con exito", response = Item.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {
        List<Item> items = shopManager.getItemsTienda();
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(Response.Status.OK)
                .entity(entity)
                .build();
    }

    @POST
    @Path("/buy/{itemId}")
    @ApiOperation(value = "Comprar un item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Compra realizada con exito", response = MessageResponse.class),
            @ApiResponse(code = 409, message = "Error en la compra", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Username inválido", response = MessageResponse.class)

    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyItem(@PathParam("itemId") int itemId, String username) {

        if (username == null || username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageResponse("Username inválido"))
                    .build();
        }
        username = username.replace("\"", "").trim();

        try {
            shopManager.comprarItem(username, itemId);
            return Response.status(Response.Status.OK)
                    .entity(new MessageResponse("Compra realizada con exito"))
                    .build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new MessageResponse(e.getMessage()))
                    .build();
        }
    }


    @GET
    @Path("/monedas/{username}")
    @ApiOperation(value = "Obtener monedas del usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Obtener monedas usuario con exito", response = CoinsResponse.class),
            @ApiResponse(code = 404, message = "Usuario no encontrado", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Username inválido", response = MessageResponse.class)

    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoins(@PathParam("username") String username) {

        if (username == null || username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageResponse("Username inválido"))
                    .build();
        }
        username = username.replace("\"", "").trim();

        try{
            int monedas = shopManager.getMonedas(username);
            return Response.status(Response.Status.OK)
                    .entity(new CoinsResponse(monedas))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MessageResponse(e.getMessage()))
                    .build();
        }
    }


    @GET
    @Path("/perfil/{username}")
    @ApiOperation(value = "Obtener perfil usuario")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Obtener perfil usuario con exito", response = Usuario.class),
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
            Usuario u = shopManager.getPerfil(username);
            return Response.status(Response.Status.OK)
                    .entity(u)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MessageResponse(e.getMessage()))
                    .build();
        }
    }


    @GET
    @Path("/ranking")
    @ApiOperation(value = "Obtener ranking")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Obtener ranking con exito", response = Usuario.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRanking() {
        List<Usuario> ranking = shopManager.getRanking();
        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(ranking) {};
        return Response.status(Response.Status.OK)
                .entity(entity)
                .build();
    }


    @GET
    @Path("/inventario/{username}")
    @ApiOperation(value = "Obtener inventario usuari")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Obtener inventario usuario con exito", response = ItemInventario.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Usuario no econtrado", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "Username inválido", response = MessageResponse.class)

    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventario(@PathParam("username") String username) {

        if (username == null || username.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MessageResponse("Username inválido"))
                    .build();
        }
        username = username.replace("\"", "").trim();

        try{
            List<ItemInventario> inventory = shopManager.getItemByUsuario(username);
            GenericEntity<List<ItemInventario>> entity = new GenericEntity<List<ItemInventario>>(inventory) {};
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();

        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MessageResponse(e.getMessage()))
                    .build();
        }

    }
}

