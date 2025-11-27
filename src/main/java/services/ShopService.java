package services;

import database.models.Item;
import database.models.Usuario;
import manager.ShopManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
    @Path("/buy/{id}")
    @ApiOperation(
            value = "Comprar un item de la tienda",
            notes = "Permite a un usuario comprar un item específico por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item comprado correctamente"),
            @ApiResponse(code = 404, message = "Item o usuario no encontrado", response = String.class),
            @ApiResponse(code = 500, message = "Error interno del servidor", response = String.class)
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprarItem(@PathParam("id") Integer itemId, String username) {
        try {
            if (username != null) {
                username = username.replace("\"", "").trim();
            }

            shopManager.comprarItem(username, itemId);
            return Response.status(Response.Status.OK)
                    .entity("{\"message\": \"Item comprado correctamente\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    @GET
    @Path("/monedas/{username}")
    @ApiOperation(
            value = "Devuelve las monoedas que tiene un usuario",
            notes = "Devuelve las monedas que tiene un usuaria especificando su ursername"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoins(@PathParam("username") String username) {

        username = username.replace("\"", "").trim();

        int monedas = this.shopManager.getMonedas(username);

        if (monedas < 0) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Monedas insuficientes de : " + username + "\"}" + monedas).build();
        }

        return Response.ok("{\"coins\":" + monedas + "}").build();
    }
//    @GET
//    @Path("/perfil/{username}")
//    @ApiOperation(
//            value = "Devuelve perfil usuario",
//            notes = "Devuelve perfil usuario en funcion de id"
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getPerfil(@PathParam("username") String usernameJson) {
//
//        usernameJson = usernameJson.replace("\"", "").trim();
//        Usuario u = shopManager.getPerfil(usernameJson);
//
//        if (u == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("{\"error\":\"Usuario no encontrado: " + usernameJson + "\"}")
//                    .build();
//        }
//
//        String json = String.format(
//                "{" +
//                        "\"username\":\"%s\"," +
//                        "\"nombre\":\"%s\"," +
//                        "\"apellido\":\"%s\"," +
//                        "\"email\":\"%s\"," +
//                        "\"monedas\":%d," +
//                        "\"mejorPuntuacion\":%d" +
//                        "}",
//                u.getUsername(),
//                u.getNombre(),
//                u.getApellido(),
//                u.getEmail(),
//                u.getMonedas(),
//                u.getMejorPuntuacion()
//        );
//
//        return Response.ok(json).build();
//    }
@GET
@Path("/perfil/{username}")
@ApiOperation(
        value = "Devuelve perfil usuario",
        notes = "Devuelve el objeto completo del usuario buscando por su username"
)
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Perfil encontrado", response = Usuario.class),
        @ApiResponse(code = 404, message = "Usuario no encontrado", response = String.class)
})
@Produces(MediaType.APPLICATION_JSON)
public Response getPerfil(@PathParam("username") String username) {

    // Limpiamos el username por si llega con comillas extrañas
    String cleanUsername = username.replace("\"", "").trim();

    Usuario u = shopManager.getPerfil(cleanUsername);

    if (u == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Usuario no encontrado: " + cleanUsername + "\"}")
                .build();
    }

    // ¡ESTA ES LA CLAVE!
    // Pasamos el objeto 'u' directamente y Jersey crea el JSON perfecto por ti.
    return Response.status(Response.Status.OK).entity(u).build();
}

    @GET
    @Path("/ranking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRanking() {

        // agafem la llista ordenada des del manager
        List<Usuario> usuarios = shopManager.getRanking();

        // construïm el JSON manualment: [ { ... }, { ... } ]
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);

            sb.append("{")
                    .append("\"username\":\"").append(u.getUsername()).append("\",")
                    .append("\"nombre\":\"").append(u.getNombre()).append("\",")
                    .append("\"mejorPuntuacion\":").append(u.getMejorPuntuacion())
                    .append("}");

            if (i < usuarios.size() - 1) {
                sb.append(",");
            }
        }

        sb.append("]");

        return Response.ok(sb.toString()).build();
    }

}

