package manager;

import db.orm.dao.ClanDAO;
import db.orm.dao.ClanDAOImpl;
import db.orm.model.Clan;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;
import services.DTOs.ClanRankingDTO;

import java.util.List;

public class ClanManagerImpl implements ClanManager {

    private static final Logger LOGGER = Logger.getLogger(ClanManagerImpl.class);
    private static ClanManagerImpl instance;
    private ClanDAO clanDAO;

    private ClanManagerImpl() {
        this.clanDAO = ClanDAOImpl.getInstance();
    }

    public static ClanManagerImpl getInstance() {
        if (instance == null) {
            instance = new ClanManagerImpl();
            LOGGER.info("Instancia de ClanManagerImpl creada");
        }
        return instance;
    }

    @Override
    public Clan crearClan(String nombre, String descripcion) {
        LOGGER.info("Intento de crear clan: " + nombre);

        if (nombre == null || nombre.isEmpty()) {
            LOGGER.error("Error: Nombre de clan vacío");
            return null;
        }

        if (this.clanDAO.getClanByNombre(nombre) != null) {
            LOGGER.error("Error: Ya existe un clan con el nombre " + nombre);
            return null;
        }

        int id = this.clanDAO.createClan(nombre, descripcion);

        if (id > 0) {
            LOGGER.info("Clan creado con éxito con ID: " + id);
            return this.clanDAO.getClanByNombre(nombre);
        } else {
            LOGGER.error("Error al crear el clan en la base de datos");
            return null;
        }
    }

    @Override
    public Clan getClan(String nombre) {
        return this.clanDAO.getClanByNombre(nombre);
    }

    @Override
    public List<ClanRankingDTO> getRanking() {
        LOGGER.info("Solicitando Ranking de clanes...");
        return this.clanDAO.getAllClanRanking();
    }

    @Override
    public void unirseClan(String username, String clanNombre) {
        LOGGER.info("Usuario " + username + " intenta unirse al clan " + clanNombre);
        this.clanDAO.unirseClan(username, clanNombre);
    }

    @Override
    public List<Usuario> getMiembros(String clanNombre) {
        LOGGER.info("Solicitando miembros del clan: " + clanNombre);
        return this.clanDAO.getMiembros(clanNombre);
    }
}