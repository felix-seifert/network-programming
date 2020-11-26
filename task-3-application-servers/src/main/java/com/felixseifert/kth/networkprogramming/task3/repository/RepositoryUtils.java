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

    // Todo: Make name of primary key also variable
    private static final String id = "ID";

    static PreparedStatement createFindAllPreparedStatement(Connection connection, String sqlTable,
                                                            Map<String, String> sqlColumns) throws SQLException {

        return connection.prepareStatement(createFindAllString(sqlTable, new ArrayList<>(sqlColumns.keySet())));
    }

    static PreparedStatement createFindByIdPreparedStatement(Connection connection, String sqlTable,
                                                             Map<String, String> sqlColumns, Object id)
            throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                createFindByIdString(sqlTable, new ArrayList<>(sqlColumns.keySet())));

        return insertObjectAtPositionI(preparedStatement, id, 1);
    }

    static PreparedStatement createCreatePreparedStatement(Connection connection, String sqlTable,
                                                           Map<String, String> sqlColumns, Object objectToSave)
            throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                createCreateString(sqlTable, new ArrayList<>(sqlColumns.keySet())));

        return insertAllValuesExceptId(preparedStatement, sqlColumns.keySet(), objectToSave);
    }

    static PreparedStatement createUpdatePreparedStatement(Connection connection, String sqlTable,
                                                           Map<String, String> sqlColumns, Object objectToSave,
                                                           Object id) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                createUpdateSting(sqlTable, new ArrayList<>(sqlColumns.keySet())));

        preparedStatement = insertAllValuesExceptId(preparedStatement, sqlColumns.keySet(), objectToSave);

        return insertObjectAtPositionI(preparedStatement, id, sqlColumns.keySet().size());
    }

    static PreparedStatement createDeletePreparedStatement(Connection connection, String sqlTable, Object id)
            throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(createDeleteString(sqlTable));

        return insertObjectAtPositionI(preparedStatement, id, 1);
    }

    private static PreparedStatement insertAllValuesExceptId(PreparedStatement preparedStatement,
                                                             Collection<String> sqlColumns, Object objectToSave)
            throws SQLException {

        int i = 0;
        for (String column : sqlColumns) {

            // Implementation assumes that value ID is implemented as
            // AUTO_INCREMENT and does NOT have to be inserted
            if(column.equals(id)) {
                continue;
            }

            i++;

            Object value = getValueOfSQLColumn(objectToSave, column);

            preparedStatement = insertObjectAtPositionI(preparedStatement, value, i);
        }

        return preparedStatement;
    }

    private static PreparedStatement insertObjectAtPositionI(PreparedStatement preparedStatement,
                                                             Object value, int i) throws SQLException {

        if(value instanceof String) {
            preparedStatement.setString(i, ((String) value));
            return preparedStatement;
        }
        if(value instanceof Integer) {
            preparedStatement.setInt(i, ((Integer) value));
            return preparedStatement;
        }
        if(value instanceof Long) {
            preparedStatement.setLong(i, ((Long) value));
            return preparedStatement;
        }
        // Todo: Generalise conversation of enums (consider to use converter classes)
        // Todo: Generalise one-to-many relationships with collections
        if(value instanceof Set<?> && ((Set) value).stream().findFirst().get() instanceof CorrectAnswer) {
            String correctAnswers = ((Set<CorrectAnswer>) value).stream()
                    .map(CorrectAnswer::getValue)
                    .collect(Collectors.joining("<>"));
            preparedStatement.setObject(i, correctAnswers);
            return preparedStatement;
        }
//        if(object instanceof Enum) {
//            // ...
//        }
        preparedStatement.setObject(i, value);
        return preparedStatement;
    }

    private static Object getValueOfSQLColumn(Object originatingObject, String sqlColumn) {

        // Parse and execute get method for given sql column
        String methodName = "get" + Arrays.stream(sqlColumn.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining());

        Object value = null;
        try {
            value = originatingObject.getClass().getMethod(methodName).invoke(originatingObject);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return value;
    }

    private static String createFindAllString(String sqlTable, Collection<String> sqlColumns) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT ");

        Iterator<String> iterator = sqlColumns.iterator();
        while(iterator.hasNext()) {
            queryBuilder.append(abbr);
            queryBuilder.append(".");
            queryBuilder.append(iterator.next());
            if(iterator.hasNext()) queryBuilder.append(", ");
        }

        queryBuilder.append(" FROM ").append(sqlTable).append(" ").append(abbr);

        return queryBuilder.toString();
    }

    private static String createFindByIdString(String sqlTable, Collection<String> sqlColumns) {
        return createFindAllString(sqlTable, sqlColumns) + " WHERE " + abbr + "." + id + "=?";
    }

    private static String createCreateString(String sqlTable, Collection<String> sqlColumns) {

        // Implementation assumes that value ID is implemented as
        // AUTO_INCREMENT and does NOT have to be inserted
        return new StringBuilder()
                .append("INSERT INTO ").append(sqlTable)
                .append("(")
                .append(sqlColumns.stream()
                        .filter(c -> !c.equals(id))
                        .collect(Collectors.joining(", ")))
                .append(") VALUES (")
                .append(sqlColumns.stream()
                        .filter(c -> !c.equals(id))
                        .map(c -> "?")
                        .collect(Collectors.joining(", ")))
                .append(")").toString();
    }

    private static String createUpdateSting(String sqlTable, Collection<String> sqlColumns) {

        return new StringBuilder()
                .append("UPDATE ").append(sqlTable)
                .append(" SET ")
                .append(sqlColumns.stream()
                        .filter(c -> !c.equals(id))
                        .map(c -> c + "=?")
                        .collect(Collectors.joining(", ")))
                .append(" WHERE " + id + "=?")
                .toString();
    }

    private static String createDeleteString(String sqlTable) {

        return new StringBuilder()
                .append("DELETE FROM ").append(sqlTable)
                .append(" WHERE ").append(id).append("=?")
                .toString();
    }
}
