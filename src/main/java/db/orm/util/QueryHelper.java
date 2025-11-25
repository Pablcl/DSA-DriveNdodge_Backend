package db.orm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static db.orm.util.ObjectHelper.getFields;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("(");

        String [] fields = db.orm.util.ObjectHelper.getFields(entity);

        sb.append("ID");
        for (String field: fields) {
            if (!field.equalsIgnoreCase("ID")) sb.append(", ").append(field);
        }
        sb.append(") VALUES (0");

        for (String field: fields) {
            if (!field.equals("ID"))  sb.append(", ?");
        }
        sb.append(")");
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)
        return sb.toString();
    }

    public static String createQuerySELECT(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE ID = ?");

        return sb.toString();
    }


    public static String createSelectFindAll(Class theClass, HashMap params) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(theClass.getSimpleName());
        if (params != null && !params.isEmpty()) {
            query.append(" WHERE 1=1");

            for (Object key : params.keySet()) {
                query.append(" AND ").append(key).append("=?");
            }
        }

        return query.toString();
    }
    public static String updateQueryUPDATE(Object entity) {
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(entity.getClass().getSimpleName()).append(" SET ");
        String[] fields = db.orm.util.ObjectHelper.getFields(entity);
        boolean first = true;
        for (String field : fields) {
            if (field.equalsIgnoreCase("ID")) continue;
            if (!first) {
                sb.append(", ");
            }
            sb.append(field).append("=?");
            first = false;
        }
        sb.append(" WHERE ID= ?");
        return sb.toString();
    }
}
