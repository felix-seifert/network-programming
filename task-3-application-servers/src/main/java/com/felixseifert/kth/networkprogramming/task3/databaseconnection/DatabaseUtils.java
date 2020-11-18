package com.felixseifert.kth.networkprogramming.task3.databaseconnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtils {

    public static Connection getConnection() {
        // Change database to accommodate desires
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

    public static void printSQLException(SQLException exception) {
        H2JDBCUtils.printSQLException(exception);
    }
}
