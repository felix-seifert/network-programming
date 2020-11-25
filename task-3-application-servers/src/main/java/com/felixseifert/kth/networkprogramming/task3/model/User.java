package com.felixseifert.kth.networkprogramming.task3.model;


import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class User implements Serializable {

    public static final String SQL_TABLE = "USERS";

    public static final Map<String, String> SQL_COLUMNS = new LinkedHashMap<>();

    static {
        SQL_COLUMNS.put("ID", "IDENTITY NOT NULL PRIMARY KEY");
        SQL_COLUMNS.put("USERNAME", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("PASSWORD", "VARCHAR(255) NOT NULL");
    }

    private Integer id;

    private String username;

    private String password;

    public User() {
    }

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static User createOutOfResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("ID");
        String username = resultSet.getString("USERNAME");
        String password = resultSet.getString("PASSWORD");
        return new User(id, username, password);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 22;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
