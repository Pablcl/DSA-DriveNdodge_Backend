package db.orm;



import db.orm.util.ObjectHelper;
import db.orm.util.QueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    public void save(Object entity) {


        // INSERT INTO Partida () ()
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)


        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);

            int i = 1;

            for (String field: ObjectHelper.getFields(entity)) {
                pstm.setObject(i++, ObjectHelper.getter(entity, field));
            }

            pstm.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close() {

    }

    @Override
    public Object get(Class theClass, Object ID) {
        return null;
    }

    public Object get(Class theClass, int ID) {

        // 1. Preparem un mapa amb l'ID
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", ID); // Asumim que la columna/variable es diu "id" o "ID"

        // 2. Reutilitzem la lògica del findAll que ja funciona!
        List<Object> result = this.findAll(theClass, params);

        // 3. Si la llista no és buida, tornem el primer objecte
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }

        return null; // No trobat
    }

    public void update(Object object) {

    }

    public void delete(Object object) {

    }

    @Override
    public List<Object> findAll(Class theClass, HashMap params) {
        String theQuery = QueryHelper.createSelectFindAll(theClass, params);
        List<Object> resultList = new ArrayList<>();
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(theQuery);
            int i = 1;
            if (params != null) {
                for (Object value : params.values()) {
                    pstm.setObject(i++, value);
                }
            }
            ResultSet rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();

            while (rs.next()) {
                Object entity = theClass.getDeclaredConstructor().newInstance();
                for (int j = 1; j <= numColumns; j++) {
                    String columnName = rsmd.getColumnName(j); // ex: "username"
                    Object columnValue = rs.getObject(j);      // ex: "Arnau"

                    ObjectHelper.setter(entity, columnName, columnValue);
                }

                resultList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
    @Override
    public List<Object> findAll(Class theClass) {
        // Reutilitzem el codi passant un HashMap null o buit
        return this.findAll(theClass, null);
    }
    public List<Object> query(String query, Class theClass, HashMap params) {
        return null;
    }
}
