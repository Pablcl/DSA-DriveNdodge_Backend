package services;

import db.orm.model.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import manager.PerfilManagerImpl;
import manager.RankingManagerImpl;
import services.DTOs.UsuariosRankingDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/ranking", description = "Servicios de ranking de usuarios")
@Path("/ranking")
public class RankingService {

    private final RankingManagerImpl rankingManager;

    public RankingService() {
        this.rankingManager = RankingManagerImpl.getInstance();
    }

    @GET
    @Path("/lista")
    @ApiOperation(value = "Obtener ranking")
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Obtener ranking con exito", response = UsuariosRankingDTO.class, responseContainer = "List")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRanking() {
        List<Usuario> ranking = rankingManager.getRanking();

        List<UsuariosRankingDTO> dtoList = new java.util.ArrayList<>();
        for (Usuario u : ranking) {
            dtoList.add(new UsuariosRankingDTO(u));
        }

        GenericEntity<List<UsuariosRankingDTO>> entity = new GenericEntity<List<UsuariosRankingDTO>>(dtoList) {};
        return Response.status(Response.Status.OK)
                .entity(entity)
                .build();
    }
}
