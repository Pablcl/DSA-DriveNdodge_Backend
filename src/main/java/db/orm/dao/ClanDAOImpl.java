package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Clan;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;
import services.DTOs.ClanRankingDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClanDAOImpl implements ClanDAO {
    private static final Logger logger = Logger.getLogger(ClanDAOImpl.class);
    private static ClanDAOImpl instance;

    private ClanDAOImpl() {
        logger.debug("Instancia de ClanDAOImpl creada");
    }

    public static ClanDAOImpl getInstance() {
        if (instance == null) {
            logger.info("Creando instancia Singleton de ClanDAOImpl");
            instance = new ClanDAOImpl();
        }
        return instance;
    }

    @Override
    public int createClan(String nombre, String descripcion) {
        logger.info("Creando clan: " + nombre);
        Session session = null;
        int clanId = 0;
        try {
            session = FactorySession.openSession();
            Clan clan = new Clan(nombre, descripcion);

            logger.debug("Guardando clan con nombre: " + nombre + ", descripcion: " + descripcion);
            session.save(clan);
            clanId = clan.getId();

        } catch (Exception e) {
            logger.error("Error al crear clan " + nombre + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return clanId;
    }

    @Override
    public Clan getClanByNombre(String nombre) {
        logger.info("Buscando clan por nombre: " + nombre);
        Session session = null;
        Clan clan = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("nombre", nombre);

            List<Object> result = session.findAll(Clan.class, params);
            if (result != null && !result.isEmpty()) {
                clan = (Clan) result.get(0);
                logger.info("Clan encontrado: " + clan.getNombre() + " (ID: " + clan.getId() + ")");
            } else {
                logger.warn("No se encontró clan con nombre: " + nombre);
            }
        } catch (Exception e) {
            logger.error("Error al buscar clan por nombre " + nombre + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return clan;
    }

    @Override
    public List<ClanRankingDTO> getAllClanRanking() {
        logger.info("Obteniendo ranking de todos los clanes");
        Session session = null;
        List<ClanRankingDTO> ranking = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            String sql = "SELECT c.ID, c.nombre, c.descripcion, c.imagen, " +
                    "COALESCE(SUM(u.mejorPuntuacion), 0) as puntosTotales " +
                    "FROM Clan c " +
                    "LEFT JOIN Usuario u ON c.ID = u.clanId " +
                    "GROUP BY c.ID " +
                    "ORDER BY puntosTotales DESC";
            //logger.debug("Query SQL para ranking de clanes: " + sql);

            ranking = (List<ClanRankingDTO>)(List<?>) session.query(ClanRankingDTO.class, sql, null);
            logger.info("Se obtuvieron " + ranking.size() + " clanes en el ranking");

        } catch (Exception e) {
            logger.error("Error al obtener ranking de clanes: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
                logger.debug("Sesión cerrada en getAllClanRanking()");
            }
        }
        return ranking;
    }

    @Override
    public void unirseClan(String username, String clanNombre) {
        logger.info("Usuario " + username + " intentando unirse al clan " + clanNombre);
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
                logger.info("Usuario " + username + " se ha unido correctamente al clan " + clanNombre + " (ID: " + c.getId() + ")");
            } else {
                if (u == null) {
                    logger.warn("No se encontró usuario con username: " + username);
                }
                if (c == null) {
                    logger.warn("No se encontró clan con nombre: " + clanNombre);
                }
            }

        } catch (Exception e) {
            logger.error("Error al unir usuario " + username + " al clan " + clanNombre + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Usuario> getMiembros(String clanNombre) {
        logger.info("Obteniendo miembros del clan: " + clanNombre);
        Session session = null;
        List<Usuario> miembros = new ArrayList<>();
        try {
            Clan c = getClanByNombre(clanNombre);
            if (c == null) {
                logger.warn("No se puede obtener miembros: clan " + clanNombre + " no encontrado");
                return miembros;
            }

            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("clanId", c.getId());

            miembros = (List<Usuario>)(List<?>) session.findAll(Usuario.class, params);
            logger.info("Se obtuvieron " + miembros.size() + " miembros del clan " + clanNombre);

        } catch (Exception e) {
            logger.error("Error al obtener miembros del clan " + clanNombre + ": " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return miembros;
    }
}