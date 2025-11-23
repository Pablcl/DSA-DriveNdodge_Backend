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
            if (!field.equals("ID")) sb.append(", ").append(field);
        }
        sb.append(") VALUES (?");

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

        // --- PROTECCIÓ CLAU: NOMÉS AFEGIM EL WHERE SI PARAMS NO ÉS NULL ---
        if (params != null && !params.isEmpty()) {
            query.append(" WHERE 1=1");

            for (Object key : params.keySet()) {
                query.append(" AND ").append(key).append("=?");
            }
        }
        // ------------------------------------------------------------------

        return query.toString();
    }
}
