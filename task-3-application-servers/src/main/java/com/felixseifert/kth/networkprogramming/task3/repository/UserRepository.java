package com.felixseifert.kth.networkprogramming.task3.repository;

import com.felixseifert.kth.networkprogramming.task3.databaseconnection.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    Connection connection = DatabaseUtils.getConnection();
    String sql = "select * from public.users where username = ? and password = ? and email = ?";
    String createUserSql= "insert into public.users (username, password, email) values (?,?,?)";

    public Boolean validateCredentials(String username, String password, String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return  true;
        }
        return  false;

    }

    public void createUser(String username, String password, String email ) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(createUserSql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();
    }
}
