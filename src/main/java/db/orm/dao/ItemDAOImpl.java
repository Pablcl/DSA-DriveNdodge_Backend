package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Item;
import db.orm.model.Usuario;
import manager.ShopManagerImpl;
import org.apache.log4j.Logger;
import org.slf4j.event.LoggingEvent;

import java.util.List;

public class ItemDAOImpl implements IItemDAO {
    private static final Logger LOGGER = Logger.getLogger(ShopManagerImpl.class);

    private static ItemDAOImpl instance;
    private ItemDAOImpl() {}

    public static ItemDAOImpl getInstance() {
        if (instance == null) instance = new ItemDAOImpl();
        return instance;
    }

    @Override
    public List<Item> getItems() {
        Session session = FactorySession.openSession();
        List<Item> items = null;
        try {
            items = (List<Item>)(List<?>) session.findAll(Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return items;
    }
    @Override
    public Item getItem(int id) {
        Session session = null;
        Item item = null;
        try {
            session = FactorySession.openSession();

            LOGGER.info("--> [DEBUG DAO] Buscant Item amb ID: " + id);

            // Log abans de cridar a l'ORM
            item = (Item) session.get(Item.class, id);

            if (item == null) {
                LOGGER.info("--> [DEBUG DAO] ALERTA: session.get() ha retornat NULL.");
            } else {
                LOGGER.info("--> [DEBUG DAO] ÈXIT: Item trobat -> " + item.getNombre());
                LOGGER.info("--> [DEBUG DAO] Dades internes: ID=" + item.getId() + ", Preu=" + item.getPrecio());
            }

        } catch (Exception e) {
            LOGGER.info("--> [DEBUG DAO] EXCEPCIÓ: Algo ha petat dins de session.get()");
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return item;
    }

}