package com.felixseifert.kth.networkprogramming.task3.databaseconnection;

import com.felixseifert.kth.networkprogramming.task3.model.Question;
import com.felixseifert.kth.networkprogramming.task3.model.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DatabaseSetup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder questionTableCreateQuery = new StringBuilder();
        questionTableCreateQuery.append("CREATE TABLE ");
        questionTableCreateQuery.append(Question.SQL_TABLE);
        questionTableCreateQuery.append("(");
        questionTableCreateQuery.append(Question.SQL_COLUMNS.entrySet().stream()
                .map(c -> c.getKey() + " " + c.getValue())
                .collect(Collectors.joining(", ")));
        questionTableCreateQuery.append(");");

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(questionTableCreateQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        this.setupUserTable();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        StringBuilder questionTableDropQuery = new StringBuilder();
        questionTableDropQuery.append("DROP TABLE ");
        questionTableDropQuery.append(Question.SQL_TABLE);

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(questionTableDropQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        this.destroyUserTable();
    }

    public void setupUserTable(){
        StringBuilder userTableCreateQuery = new StringBuilder();
        userTableCreateQuery.append("CREATE TABLE ");
        userTableCreateQuery.append(User.SQL_TABLE);
        userTableCreateQuery.append("(");
        userTableCreateQuery.append(User.SQL_COLUMNS.entrySet().stream()
                .map(c -> c.getKey() + " " + c.getValue())
                .collect(Collectors.joining(", ")));
        userTableCreateQuery.append(");");

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(userTableCreateQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public void destroyUserTable(){
        StringBuilder userTableDropQuery = new StringBuilder();
        userTableDropQuery.append("DROP TABLE ");
        userTableDropQuery.append(User.SQL_TABLE);

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(userTableDropQuery.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
