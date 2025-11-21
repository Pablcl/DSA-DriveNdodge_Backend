package database;

import database.models.Item;
import database.models.Usuario;
import java.util.List;

public interface BaseDeDatos {
    //Auth
    void addUsuario(Usuario usuario);
    Usuario getUsuario(String username);
    void removeUsuario(String username);
    void updateUsuario(Usuario usuario);
    List<Usuario> getUsuarios();
    //SHOP
    List<Item> getItems();
    Item getItem(Integer id);
    void addItem(Item item);
}
