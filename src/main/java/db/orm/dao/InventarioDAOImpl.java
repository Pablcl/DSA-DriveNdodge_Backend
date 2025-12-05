package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.SessionImpl;
import db.orm.model.Inventario;
import manager.ShopManagerImpl;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class InventarioDAOImpl implements InventarioDAO {
    private static InventarioDAOImpl instance;
    private static final Logger LOGGER = Logger.getLogger(ShopManagerImpl.class);

    private InventarioDAOImpl() {
    }
    public static InventarioDAOImpl getInstance() {
        if (instance == null) {
            instance = new InventarioDAOImpl();
        }return instance;
    }

    public int addInventario(Inventario inventario) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.save(inventario);
            LOGGER.info("Inventario guardat correctament");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }


    public List<Inventario> getInventario(int usuarioId) {
        Session session= null;
        List<Inventario> listaInventario = null;
        try {
            session = FactorySession.openSession();
            HashMap<String, Object> params = new HashMap<>();
            params.put("usuarioId", usuarioId);

            listaInventario = session.findAll(Inventario.class, params);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return listaInventario;
    }
}
