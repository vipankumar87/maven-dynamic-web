package com.rudracomputer.webblog.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple JDBC connection utility.
 * Configure DB_URL / DB_USER / DB_PASS for your environment.
 *
 * For production consider a connection pool (HikariCP, Tomcat JDBC Pool, or JNDI DataSource).
 */
public final class DBConnection {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/webblog?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
