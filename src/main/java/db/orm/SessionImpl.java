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
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);
            int i = 1;

            // Recorrem els camps i omplim els interrogants
            for (String field : ObjectHelper.getFields(entity)) {
                // Saltem l'ID perquè ja hem posat un '0' hardcoded a la query
                if (field.equalsIgnoreCase("ID")) continue;

                pstm.setObject(i++, ObjectHelper.getter(entity, field));
            }

            // IMPORTANT: Fem executeUpdate() per a INSERTS, no executeQuery()
            pstm.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(Class theClass, Object ID) {
        Object entity = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            entity = theClass.getDeclaredConstructor().newInstance();
            String selectQuery = QueryHelper.createQuerySELECT(entity);
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, ID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int numColumns = rsmd.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    ObjectHelper.setter(entity, columnName, columnValue);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    public void update(Object entity) {
        String updateQuery = QueryHelper.updateQueryUPDATE(entity);
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(updateQuery);
            String[] fields = ObjectHelper.getFields(entity);
            int i = 1;
            for (String field : fields) {
                if (!field.equalsIgnoreCase("ID")) {
                    pstm.setObject(i++, ObjectHelper.getter(entity, field));
                }
            }
            // Assumim que l'objecte té un mètode getId() o getID()
            // ObjectHelper sol trobar-ho per reflexió si existeix "ID" o "id"
            Object idValue = null;
            try { idValue = ObjectHelper.getter(entity, "ID"); } catch (Exception e) {}
            if (idValue == null) {
                try { idValue = ObjectHelper.getter(entity, "id"); } catch (Exception e) {}
            }

            pstm.setObject(i, idValue);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    String columnName = rsmd.getColumnName(j);
                    Object columnValue = rs.getObject(j);
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
        return this.findAll(theClass, null);
    }

    @Override
    public List<Object> query(String query, Class theClass, HashMap params) {
        return null;
    }
}