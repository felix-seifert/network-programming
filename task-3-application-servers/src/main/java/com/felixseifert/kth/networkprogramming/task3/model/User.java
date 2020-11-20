package com.felixseifert.kth.networkprogramming.task3.model;


import javax.persistence.*;
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
        SQL_COLUMNS.put("EMAIL", "VARCHAR(255) NOT NULL");
    }


    protected int id;
    private String username;
    private String password;
    private String email;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
