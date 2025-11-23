package db.orm.dao;
import db.orm.model.Item;
import java.util.List;

public interface IItemDAO {
    List<Item> getItems();
}