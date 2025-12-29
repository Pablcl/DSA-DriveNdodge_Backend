package manager;

import db.orm.model.Clan;
import db.orm.model.Usuario;
import services.DTOs.ClanRankingDTO;

import java.util.List;

public interface ClanManager {
    Clan crearClan(String nombre, String descripcion);
    Clan getClan(String nombre);
    List<ClanRankingDTO> getRanking();
    void unirseClan(String username, String clanNombre);
    List<Usuario> getMiembros(String clanNombre);
}