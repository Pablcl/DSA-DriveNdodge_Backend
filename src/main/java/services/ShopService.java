package services;


import db.orm.model.Item;
import db.orm.model.Usuario;

import manager.ShopManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import services.DTOs.CoinsResponse;
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
            @ApiResponse(code = 200, message = "OK", response = Item.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {
        List<Item> items = shopManager.getItemsTienda();
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @Path("/buy/{itemId}")
    @ApiOperation(value = "Comprar un item")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyItem(@PathParam("itemId") int itemId, String username) {
        if (username != null) username = username.replace("\"", "").trim();

        try {
            shopManager.comprarItem(username, itemId);

            // Retornem un objecte missatge net
            return Response.status(200).entity(new MessageResponse("Compra realitzada amb èxit")).build();

        } catch (RuntimeException e) {
            return Response.status(409).entity(new MessageResponse(e.getMessage())).build();
        }
    }
    @GET
    @Path("/monedas/{username}")
    @ApiOperation(value = "Obtenir monedes de l'usuari")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoins(@PathParam("username") String username) {
        if (username != null) username = username.replace("\"", "").trim();

        int monedas = shopManager.getMonedas(username);

        if (monedas < 0) {
            return Response.status(404).entity(new MessageResponse("Usuari no trobat")).build();
        }

        // AQUÍ ESTÀ LA MÀGIA: Retornem un objecte, no un Map
        return Response.ok(new CoinsResponse(monedas)).build();
    }
    @GET
    @Path("/perfil/{username}")
    @ApiOperation(value = "Obtenir perfil d'usuari")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerfil(@PathParam("username") String username) {
        Usuario u = shopManager.getPerfil(username);
        System.out.println("Buscant perfil amb usuari " + u.getUsername());
        if (u != null) {

            return Response.status(200).entity(u).build();
        } else {
            return Response.status(404).entity("Usuari no trobat").build();
        }
    }
    @GET
    @Path("/ranking")
    @ApiOperation(value = "Obtenir ranking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRanking() {
        List<Usuario> ranking = shopManager.getRanking();
        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(ranking) {};
        return Response.status(200).entity(entity).build();
    }

}

