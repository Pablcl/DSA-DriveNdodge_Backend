package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Clan;
import db.orm.model.Usuario;
import services.DTOs.ClanRankingDTO;

import java.util.HashMap;
import java.util.List;

public class ClanDAOImpl implements ClanDAO {

    private static ClanDAOImpl instance;

    private ClanDAOImpl() {
    }

    public static ClanDAOImpl getInstance() {
        if (instance == null) {
            instance = new ClanDAOImpl();
        }
        return instance;
    }

    @Override
    public int createClan(String nombre, String descripcion) {
        Session session = null;
        int clanId = 0;
        try {
            session = FactorySession.openSession();
            Clan clan = new Clan(nombre, descripcion);
            session.save(clan);

            Clan c = this.getClanByNombre(nombre);
            if(c != null) clanId = c.getId();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return clanId;
    }

    @Override
    public Clan getClanByNombre(String nombre) {
        Session session = null;
        Clan clan = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("nombre", nombre);

            List<Object> result = session.findAll(Clan.class, params);
            if (result != null && !result.isEmpty()) {
                clan = (Clan) result.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return clan;
    }

    @Override
    public List<ClanRankingDTO> getAllClanRanking() {
        Session session = null;
        List<ClanRankingDTO> ranking = null;
        try {
            session = FactorySession.openSession();
            String sql = "SELECT c.ID, c.nombre, c.descripcion, c.imagen, " +
                    "COALESCE(SUM(u.mejorPuntuacion), 0) as puntosTotales " +
                    "FROM Clan c " +
                    "LEFT JOIN Usuario u ON c.ID = u.clanId " +
                    "GROUP BY c.ID " +
                    "ORDER BY puntosTotales DESC";

            ranking = (List<ClanRankingDTO>)(List<?>) session.query(ClanRankingDTO.class, sql, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return ranking;
    }

    @Override
    public void unirseClan(String username, String clanNombre) {
        Session session = null;
        try {
            session = FactorySession.openSession();

            HashMap<String, Object> paramsU = new HashMap<>();
            paramsU.put("username", username);
            List<Object> users = session.findAll(Usuario.class, paramsU);
            Usuario u = users.isEmpty() ? null : (Usuario) users.get(0);

            HashMap<String, Object> paramsC = new HashMap<>();
            paramsC.put("nombre", clanNombre);
            List<Object> clanes = session.findAll(Clan.class, paramsC);
            Clan c = clanes.isEmpty() ? null : (Clan) clanes.get(0);

            if (u != null && c != null) {
                u.setClanId(c.getId());
                session.update(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Usuario> getMiembros(String clanNombre) {
        Session session = null;
        List<Usuario> miembros = null;
        try {
            Clan c = getClanByNombre(clanNombre);
            if (c == null) return null;

            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("clanId", c.getId());

            miembros = (List<Usuario>)(List<?>) session.findAll(Usuario.class, params);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return miembros;
    }
}