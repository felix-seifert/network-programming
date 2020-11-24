package com.felixseifert.kth.networkprogramming.task3.repository;

import com.felixseifert.kth.networkprogramming.task3.databaseconnection.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    Connection connection = DatabaseUtils.getConnection();
    String validateSql = "select * from public.users where username = ? and password = ? and email = ?";
    String createUserSql= "insert into public.users (username, password, email) values (?,?,?)";

    public Boolean validateCredentials(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(validateSql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next() ? true : false;
    }

    public void createUser(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(createUserSql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
    }
}
