package manager;


import db.orm.dao.IItemDAO;
import db.orm.dao.IUsuarioDAO;
import db.orm.dao.IItemDAO;
import db.orm.dao.ItemDAOImpl;
import db.orm.dao.UsuarioDAOImpl;
import db.orm.model.Item;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ShopManagerImpl implements ShopManager {
    private static final Logger LOGGER = Logger.getLogger(ShopManagerImpl.class);

    private static ShopManagerImpl instance;
    private final IItemDAO itemDAO;
    private final IUsuarioDAO usuarioDAO;

    private ShopManagerImpl() {
        this.usuarioDAO = UsuarioDAOImpl.getInstance();
        this.itemDAO = ItemDAOImpl.getInstance();
    }

    public static ShopManagerImpl getInstance() {
        if (instance == null) {
            instance = new ShopManagerImpl();
            LOGGER.info("Instancia de ShopManagerImpl creada");
        }
        return instance;
    }

    @Override
    public List<Item> getItemsTienda() {
        LOGGER.info("Obteniendo lista de items de la tienda");
        return itemDAO.getItems();
    }

    @Override
    public void comprarItem(String username, int itemId) {
        Usuario usuario = usuarioDAO.getUsuario(itemId);
        if (usuario == null) {
            LOGGER.error("Intento de compra fallido: Usuario no encontrado: " + username);
            throw new RuntimeException("Usuario no encontrado");
        }

        Item item = null; //usuario.getItem(itemId);
        if (item == null) {
            LOGGER.error("Intento de compra fallido: Item no encontrado: " + itemId);
            throw new RuntimeException("Item no encontrado");
        }
        int monedas = usuario.getMonedas();
        if (monedas < item.getPrecio()) throw new RuntimeException("Monedas insuficientes");;

        usuario.setMonedas(monedas - item.getPrecio());

        LOGGER.info("Usuario '" + username + "' ha comprado el item: " + item);
        // Aquí se implementaría la lógica de compra (descontar dinero, añadir item al inventario, etc.)
    }
    @Override
    public int getMonedas(String username) {
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        LOGGER.info("monedas:"+ u.getMonedas());
        if (u == null) return -1;
        return u.getMonedas();
    }
    @Override
    public Usuario getPerfil(String username){
        return this.usuarioDAO.getUsuarioByUsername(username);
    }

    public int getMejorPuntuacion(String username) {
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        if (u == null) return -1;
        return u.getMejorPuntuacion();
    }
    @Override
    public List<Usuario> getRanking() {
        List<Usuario> ranking = usuarioDAO.getUsuarios();
        if (ranking != null){
            ranking.sort((u1,u2) -> Integer.compare(u2.getMejorPuntuacion(), u1.getMejorPuntuacion()));
        }
        return ranking;
    }


}

