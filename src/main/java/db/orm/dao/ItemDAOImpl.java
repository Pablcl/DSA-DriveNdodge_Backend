package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Item;
import java.util.List;

public class ItemDAOImpl implements IItemDAO {

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
            // Fem servir el findAll genèric
            // Nota: Cal fer el cast (List<Item>)(List<?>) perquè findAll retorna Objects
            items = (List<Item>)(List<?>) session.findAll(Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return items;
    }
}