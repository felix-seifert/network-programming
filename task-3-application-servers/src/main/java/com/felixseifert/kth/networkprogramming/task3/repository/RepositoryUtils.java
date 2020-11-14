package com.felixseifert.kth.networkprogramming.task3.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

public class RepositoryUtils {

    private static final String abbr = "e";

    static PreparedStatement createFindAllPreparedStatement(Connection connection, String sqlTable,
                                                            String[] sqlColumns) throws SQLException {

        return connection.prepareStatement(createFindAllString(sqlTable, sqlColumns));
    }

    static PreparedStatement createFindByIdPreparedStatement(Connection connection, String sqlTable,
                                                             String[] columns, Object id) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(createFindByIdString(sqlTable, columns));

        if(id instanceof Integer) {
            preparedStatement.setInt(1, (Integer) id);
        }
        else if(id instanceof Long) {
            preparedStatement.setLong(1, (Long) id);
        }
        else if(id instanceof String) {
            preparedStatement.setString(1, (String) id);
        }

        return preparedStatement;
    }

    private static String createFindAllString(String sqlTable, String[] sqlColumns) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ");

        Iterator<String> iterator = Arrays.stream(sqlColumns).iterator();
        while(iterator.hasNext()) {
            queryBuilder.append(abbr);
            queryBuilder.append(".");
            queryBuilder.append(iterator.next());
            if(iterator.hasNext()) queryBuilder.append(", ");
        }

        queryBuilder.append(" FROM ");
        queryBuilder.append(sqlTable);
        queryBuilder.append(" ");
        queryBuilder.append(abbr);

        return queryBuilder.toString();
    }

    private static String createFindByIdString(String sqlTable, String[] sqlColumns) {
        return createFindAllString(sqlTable, sqlColumns) + " WHERE " + abbr + ".ID=?";
    }
}
