package db.orm.dao;

import db.orm.model.Clan;
import db.orm.model.Usuario;
import services.DTOs.ClanRankingDTO;

import java.util.List;

public interface ClanDAO {
    int createClan(String nombre, String descripcion);
    Clan getClanByNombre(String nombre);
    List<ClanRankingDTO> getAllClanRanking();
    void unirseClan(String username, String clanNombre);
    List<Usuario> getMiembros(String clanNombre);
}