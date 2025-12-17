package manager;

import db.orm.model.Usuario;

import java.util.List;

public interface RankingManager {
    List<Usuario> getRanking();
}
