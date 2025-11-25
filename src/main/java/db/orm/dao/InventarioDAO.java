package db.orm.dao;

import db.orm.model.Inventario;
import db.orm.model.Item;

import java.util.List;

public interface InventarioDAO {
    int addInventario(Inventario inventario);
    List<Inventario> getInventario(int usuarioId);

}
