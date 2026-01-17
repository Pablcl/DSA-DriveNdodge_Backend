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
    public int createClan(String nombre, String descripcion, String imagen) {
        logger.info("Creando clan: " + nombre);
        Session session = null;
        int clanId = 0;
        try {
            session = FactorySession.openSession();
            Clan clan = new Clan(nombre, descripcion, imagen);

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
            Clan nuevoClan = clanes.isEmpty() ? null : (Clan) clanes.get(0);

            if (u != null && nuevoClan != null) {
                Integer currentId = u.getClanId();
                boolean tieneClan = (currentId != null && currentId != 0);

                if (tieneClan) {
                    if (currentId.equals(nuevoClan.getId())) {
                        logger.warn("El usuario ya pertenece a este mismo clan.");
                        return;
                    } else {
                        logger.warn("Usuario ya tiene clan (ID: " + currentId + "). Debe salir primero.");
                        throw new IllegalStateException("YA_TIENE_CLAN");
                    }
                }

                u.setClanId(nuevoClan.getId());
                session.update(u);
                logger.info("Usuario " + username + " unido a " + clanNombre);

            } else {
                logger.warn("Usuario o Clan no encontrados.");
            }

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error al unir usuario a clan: " + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }


    @Override
    public void salirClan(String username) {
        logger.info("Usuario " + username + " intentando salir de su clan");
        Session session = null;

        try {
            session = FactorySession.openSession();

            HashMap<String, Object> paramsU = new HashMap<>();
            paramsU.put("username", username);

            List<Object> users = session.findAll(Usuario.class, paramsU);
            Usuario u = users.isEmpty() ? null : (Usuario) users.get(0);

            if (u != null && u.getClanId() != null && u.getClanId() != 0) {

                int clanIdAbandonado = u.getClanId();
                u.setClanId(null);
                session.update(u);
                logger.info("Usuario " + username + " ha salido correctamente del clan");

                checkAndDeleteEmptyClan(session, clanIdAbandonado);
            } else {
                logger.warn("No se encontró usuario con username: " + username);
            }

        } catch (Exception e) {
            logger.error("Error al sacar al usuario " + username + " del clan: " + e.getMessage(), e);
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
    @Override
    public List<Clan> getAllClans() {
        logger.info("Obteniendo lista de todos los clanes");
        Session session = null;
        List<Clan> clanes = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            clanes = (List<Clan>)(List<?>) session.findAll(Clan.class);
        } catch (Exception e) {
            logger.error("Error al obtener clanes: " + e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }
        return clanes;
    }
    @Override
    public Clan getClanById(int id) {
        logger.info("Buscando clan por ID: " + id);
        Session session = null;
        Clan clan = null;
        try {
            session = FactorySession.openSession();
            clan = (Clan) session.get(Clan.class, id);
        } catch (Exception e) {
            logger.error("Error al buscar clan por ID " + id + ": " + e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }
        return clan;
    }

    private void checkAndDeleteEmptyClan(Session session, int clanId) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("clanId", clanId);
            List<Object> miembrosRestantes = session.findAll(Usuario.class, params);

            if (miembrosRestantes.isEmpty()) {
                Clan clanVacio = (Clan) session.get(Clan.class, clanId);

                if (clanVacio != null) {
                    session.delete(clanVacio);
                    logger.info("LIMPIEZA: El clan '" + clanVacio.getNombre() + "' (ID: " + clanId + ") se ha quedado vacío y ha sido eliminado.");
                }
            }
        } catch (Exception e) {
            logger.error("Error al intentar borrar clan vacío ID " + clanId, e);
        }
    }
    @Override
    public Integer getClanIdByUsername(String username) {
        Session session = null;
        Integer clanId = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);

            List<Object> users = session.findAll(Usuario.class, params);
            if (!users.isEmpty()) {
                Usuario u = (Usuario) users.get(0);
                clanId = u.getClanId();
            }
        } catch (Exception e) {
            logger.error("Error al consultar clan del usuario " + username, e);
        } finally {
            if (session != null) session.close();
        }

        return (clanId != null && clanId != 0) ? clanId : null;
    }
}