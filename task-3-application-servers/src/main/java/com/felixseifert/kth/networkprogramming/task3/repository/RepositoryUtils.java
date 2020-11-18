package com.felixseifert.kth.networkprogramming.task3.repository;

import com.felixseifert.kth.networkprogramming.task3.model.CorrectAnswer;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RepositoryUtils {

    private static final String abbr = "e";

    static PreparedStatement createFindAllPreparedStatement(Connection connection, String sqlTable,
                                                            Map<String, String> sqlColumns) throws SQLException {

        return connection.prepareStatement(createFindAllString(sqlTable, new ArrayList<>(sqlColumns.keySet())));
    }

    static PreparedStatement createFindByIdPreparedStatement(Connection connection, String sqlTable,
                                                             Map<String, String> sqlColumns, Object id) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                createFindByIdString(sqlTable, new ArrayList<>(sqlColumns.keySet())));

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

    static PreparedStatement createCreatePreparedStatement(Connection connection, String sqlTable,
            Map<String, String> sqlColumns, Object objectToSave) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                createCreateString(sqlTable, new ArrayList<>(sqlColumns.keySet())));

        try {
            int i = 0;
            for (String column : sqlColumns.keySet()) {

                // Implementation assumes that value ID is implemented as
                // AUTO_INCREMENT and does NOT have to be inserted
                if(column.equals("ID")) {
                    continue;
                }

                i++;

                // Parse and execute get method for each sql column
                String methodName = "get" + Arrays.stream(column.split("_"))
                        .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                        .collect(Collectors.joining());

                Object value = objectToSave.getClass().getMethod(methodName).invoke(objectToSave);

                preparedStatement = insertObjectAtPositionI(preparedStatement, value, i);
            }
        }
        catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    private static PreparedStatement insertObjectAtPositionI(PreparedStatement preparedStatement,
                                                             Object object, int i) throws SQLException {

        if(object instanceof String) {
            preparedStatement.setString(i, ((String) object));
            return preparedStatement;
        }
        if(object instanceof Integer) {
            preparedStatement.setInt(i, ((Integer) object));
            return preparedStatement;
        }
        if(object instanceof Long) {
            preparedStatement.setLong(i, ((Long) object));
            return preparedStatement;
        }
        // Todo: Generalise conversation of enums (consider to use converter classes)
        if(object instanceof CorrectAnswer) {
            preparedStatement.setInt(i, ((CorrectAnswer) object).getDatabaseCode());
            return preparedStatement;
        }
//        if(object instanceof Enum) {
//            // ...
//        }
        preparedStatement.setObject(i, object);
        return preparedStatement;
    }

    static String createFindAllString(String sqlTable, List<String> sqlColumns) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ");

        Iterator<String> iterator = sqlColumns.iterator();
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

    static String createFindByIdString(String sqlTable, List<String> sqlColumns) {
        return createFindAllString(sqlTable, sqlColumns) + " WHERE " + abbr + ".ID=?";
    }

    static String createCreateString(String sqlTable, List<String> sqlColumns) {

        // Implementation assumes that value ID is implemented as
        // AUTO_INCREMENT and does NOT have to be inserted
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(sqlTable);
        queryBuilder.append("(");
        queryBuilder.append(sqlColumns.stream()
                .filter(c -> !c.equals("ID"))
                .collect(Collectors.joining(", ")));
        queryBuilder.append(") VALUES (");
        queryBuilder.append(sqlColumns.stream()
                .filter(c -> !c.equals("ID"))
                .map(c -> "?")
                .collect(Collectors.joining(", ")));
        queryBuilder.append(")");

        return queryBuilder.toString();
    }
}
