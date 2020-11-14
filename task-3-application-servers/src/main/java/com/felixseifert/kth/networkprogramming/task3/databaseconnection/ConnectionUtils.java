package com.felixseifert.kth.networkprogramming.task3.databaseconnection;

import java.sql.Connection;

public class ConnectionUtils {

    public static Connection getConnection() {
        // Change database to accommodate your desires.
        return H2JDBCUtils.getConnection();
    }

    public static void closeQuietly(Connection conn) {
        try {
            conn.close();
        }
        catch (Exception ignored) {}
    }

    public static void rollbackQuietly(Connection conn) {
        try {
            conn.rollback();
        }
        catch (Exception ignored) {}
    }
}
