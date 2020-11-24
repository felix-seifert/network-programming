package com.felixseifert.kth.networkprogramming.task3.databaseconnection;

import com.felixseifert.kth.networkprogramming.task3.model.Question;
import com.felixseifert.kth.networkprogramming.task3.model.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseSetup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.createTable(Question.SQL_COLUMNS);
        this.createTable(User.SQL_COLUMNS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        this.destroyTable(Question.SQL_TABLE);
        this.destroyTable(User.SQL_TABLE);
    }

    public void createTable( Map<String, String> sqlColumns){
        StringBuilder tableCreateQuery = new StringBuilder();
        tableCreateQuery.append("CREATE TABLE ");
        tableCreateQuery.append(User.SQL_TABLE);
        tableCreateQuery.append("(");
        tableCreateQuery.append(sqlColumns.entrySet().stream()
                .map(c -> c.getKey() + " " + c.getValue())
                .collect(Collectors.joining(", ")));
        tableCreateQuery.append(");");

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(tableCreateQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public void destroyTable(String tableName){
        StringBuilder tableDropQuery = new StringBuilder();
        tableDropQuery.append("DROP TABLE ");
        tableDropQuery.append(tableName);

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(tableDropQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
