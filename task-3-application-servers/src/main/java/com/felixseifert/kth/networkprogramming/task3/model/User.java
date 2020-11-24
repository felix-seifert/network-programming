package com.felixseifert.kth.networkprogramming.task3.model;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class User implements Serializable {
    public static final String SQL_TABLE = "USERS";

    public static final Map<String, String> SQL_COLUMNS = new LinkedHashMap<>();

    static {
        SQL_COLUMNS.put("ID", "serial PRIMARY KEY");
        SQL_COLUMNS.put("USERNAME", "VARCHAR(255) NOT NULL");
        SQL_COLUMNS.put("PASSWORD", "VARCHAR(255) NOT NULL");
    }
    protected int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
