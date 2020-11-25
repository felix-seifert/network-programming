package com.felixseifert.kth.networkprogramming.task3.repository;

import com.felixseifert.kth.networkprogramming.task3.databaseconnection.DatabaseUtils;
import com.felixseifert.kth.networkprogramming.task3.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static UserRepository userRepositorySingleton;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if(userRepositorySingleton == null) {
            synchronized (UserRepository.class) {
                if(userRepositorySingleton == null) {
                    userRepositorySingleton = new UserRepository();
                }
            }
        }
        return userRepositorySingleton;
    }

    public Boolean areCredentialsValid(String username, String password) {

        String validateSql = "select * from public.users where username = ? and password = ?";

        try(Connection connection = DatabaseUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(validateSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return false;
    }

    public List<User> findAllUsers() {

        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createFindAllPreparedStatement(
                    connection, User.SQL_TABLE, User.SQL_COLUMNS);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(User.createOutOfResultSet(resultSet));
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }

        return users;
    }

    public void createUser(User user) {

        try(Connection connection = DatabaseUtils.getConnection()) {

            PreparedStatement preparedStatement = RepositoryUtils.createCreatePreparedStatement(
                    connection, User.SQL_TABLE, User.SQL_COLUMNS, user);
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
