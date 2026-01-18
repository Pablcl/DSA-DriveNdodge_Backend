package manager;


import db.orm.dao.*;
import db.orm.dao.IItemDAO;
import db.orm.dao.InventarioDAOImpl;
import db.orm.dao.InventarioDAO;
import db.orm.model.Inventario;
import db.orm.model.Item;
import db.orm.model.Usuario;

import org.apache.log4j.Logger;
import services.DTOs.ItemInventarioDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            LOGGER.error("Compra fallida: Usuario no encontrado - " + username);
            throw new RuntimeException("Usuario no encontrado");
        }

        Item item = itemDAO.getItem(itemId);
        if (item == null) {
            LOGGER.error("Compra fallida: Item no encontrado - " + itemId);
            throw new RuntimeException("Item no encontrado");
        }

        int monedas = usuario.getMonedas();
        if (monedas < item.getPrecio()) {
            throw new RuntimeException("Monedas insuficientes");
        }

        usuario.setMonedas(monedas - item.getPrecio());
        usuarioDAO.updateUsuario(usuario);
        LOGGER.info("Usuario " + username + " pagó " + item.getPrecio() + " monedas.");


        List<Inventario> misItems = inventarioDAO.getInventario(usuario.getId());

        Inventario itemExistente = null;

        for (Inventario inv : misItems) {
            if (inv.getItemId() == itemId) {
                itemExistente = inv;
                break;
            }
        }
        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + 1;
            itemExistente.setCantidad(nuevaCantidad);

            inventarioDAO.updateInventario(itemExistente);
            LOGGER.info("Actualizado item " + itemId + " a cantidad: " + nuevaCantidad);

        } else {
            Inventario nuevoInventario = new Inventario(usuario.getId(), item.getId(), 1);
            inventarioDAO.addInventario(nuevoInventario);
            LOGGER.info("Añadido nuevo item " + itemId + " al inventario.");
        }
    }


    @Override
    public int getMonedas(String username) {
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);

        if (u == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        LOGGER.info("monedas:"+ u.getMonedas());
        return u.getMonedas();
    }


    public int getMejorPuntuacion(String username) {
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        if (u == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return u.getMejorPuntuacion();
    }

    @Override
    public List<ItemInventarioDTO> getItemByUsuario(String username) {
        Usuario u = this.usuarioDAO.getUsuarioByUsername(username);
        if (u == null) return new ArrayList<>();

        List<Inventario> inventarioList = this.inventarioDAO.getInventario(u.getId());

        List<ItemInventarioDTO> resultado = new ArrayList<>();

        if (inventarioList != null) {
            for (Inventario inv : inventarioList) {
                Item item = itemDAO.getItem(inv.getItemId());

                if (item != null) {
                    ItemInventarioDTO dto = new ItemInventarioDTO(item, inv.getCantidad());
                    resultado.add(dto);
                }
            }
        }

        return resultado;
    }


}

