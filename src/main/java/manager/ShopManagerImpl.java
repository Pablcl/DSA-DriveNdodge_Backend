package manager;


import db.orm.dao.*;
import db.orm.dao.IItemDAO;
import db.orm.dao.InventarioDAOImpl;
import db.orm.dao.InventarioDAO;
import db.orm.model.Inventario;
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
    private final InventarioDAO inventarioDAO;

    private ShopManagerImpl() {
        this.usuarioDAO = UsuarioDAOImpl.getInstance();
        this.itemDAO = ItemDAOImpl.getInstance();
        this.inventarioDAO = InventarioDAOImpl.getInstance();
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
        Usuario usuario = usuarioDAO.getUsuarioByUsername(username);
        if (usuario == null) {
            LOGGER.error("Intento de compra fallido: Usuario no encontrado: " + username);
            throw new RuntimeException("Usuario no encontrado");
        }

        Item item = itemDAO.getItem(itemId);
        if (item == null) {
            LOGGER.error("Intento de compra fallido: Item no encontrado: " + itemId);
            throw new RuntimeException("Item no encontrado");
        }
        int monedas = getMonedas(username);
        if (monedas < item.getPrecio()) throw new RuntimeException("Monedas insuficientes");;
        usuario.setMonedas(monedas-item.getPrecio());
        usuarioDAO.updateUsuario(usuario);
        LOGGER.info("Monedas de usuario: " + usuarioDAO.getUsuarioByUsername(username).getMonedas());
        Inventario inventario = new Inventario(usuario.getId(), item.getId());
        inventarioDAO.addInventario(inventario);
        LOGGER.info("Usuario '" + username + "' ha comprado el item: " + item);


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
        List<Usuario> ranking = usuarioDAO.getUsuariosRanking();
        return ranking;
    }
    public List<Item> getItemByUsuario(String username){
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        LOGGER.info("Obtingent items de inventari de: " + u.getUsername());
        if (u == null) return null;
        List<Inventario> inventarioList = this.inventarioDAO.getInventario(u.getId());
        List<Item> itemList = new ArrayList<>();
        if(inventarioList != null){
            for(Inventario inventario : inventarioList){
                Item item = itemDAO.getItem(inventario.getId());
                if(inventarioList != null){
                    itemList.add(item);
                    LOGGER.info("Item en inventario encontrado " + item.getNombre());
                }
            }
        }return itemList;
    }


}

