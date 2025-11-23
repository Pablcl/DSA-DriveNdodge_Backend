package manager;



import db.orm.model.Item;
import db.orm.model.Usuario;

import java.util.List;

public interface ShopManager {
    List<Item> getItemsTienda();
    void comprarItem(String username, int itemId);
    int getMonedas(String username);
    Usuario getPerfil(String username);
    List<Usuario> getRanking();

}

