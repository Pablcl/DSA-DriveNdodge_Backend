package manager;

import db.orm.dao.IUsuarioDAO;
import db.orm.dao.UsuarioDAOImpl;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.util.List;

public class RankingManagerImpl implements RankingManager {

    private static final Logger LOGGER = Logger.getLogger(RankingManagerImpl.class);

    private static RankingManagerImpl instance;
    private final IUsuarioDAO usuarioDAO;

    private RankingManagerImpl() {
        this.usuarioDAO = UsuarioDAOImpl.getInstance();
    }

    public static RankingManagerImpl getInstance() {
        if (instance == null) {
            instance = new RankingManagerImpl();
            LOGGER.info("Instancia de RankingManagerImpl creada");
        }
        return instance;
    }

    @Override
    public List<Usuario> getRanking() {
        List<Usuario> ranking = usuarioDAO.getUsuariosRanking();
        return ranking;
    }

}
