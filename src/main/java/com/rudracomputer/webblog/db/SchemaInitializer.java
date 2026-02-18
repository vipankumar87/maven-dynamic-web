package com.rudracomputer.webblog.db;

import com.rudracomputer.webblog.util.PasswordUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs once at application startup.
 * Creates the 'webblog' database and 'users' table if they don't exist,
 * then inserts a default admin account when the table is empty.
 */
@WebListener
public class SchemaInitializer implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(SchemaInitializer.class.getName());

    // Root-level URL with no database selected so we can CREATE DATABASE
    private static final String BASE_URL =
            "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "MySQL JDBC driver not found – schema init skipped", e);
            return;
        }

        createDatabase();
        createTables();
        seedAdmin();
    }

    // ---------------------------------------------------------------- helpers

    private void createDatabase() {
        try (Connection con = DriverManager.getConnection(BASE_URL, DB_USER, DB_PASS);
             Statement st = con.createStatement()) {

            st.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS webblog " +
                "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
            );
            LOG.info("[SchemaInitializer] Database 'webblog' is ready.");

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SchemaInitializer] Could not create database", e);
        }
    }

    private void createTables() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  id                 VARCHAR(36)          NOT NULL," +
                "  name               VARCHAR(100)         NOT NULL," +
                "  email              VARCHAR(150)         NOT NULL," +
                "  password_hash      VARCHAR(255)         NOT NULL," +
                "  role               ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER'," +
                "  reset_token        VARCHAR(100)         DEFAULT NULL," +
                "  reset_token_expiry DATETIME             DEFAULT NULL," +
                "  created_at         DATETIME             NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (id)," +
                "  UNIQUE KEY uq_users_email (email)," +
                "  INDEX idx_users_reset_token (reset_token)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );
            LOG.info("[SchemaInitializer] Table 'users' is ready.");

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SchemaInitializer] Could not create tables", e);
        }
    }

    private void seedAdmin() {
        String sql = "INSERT IGNORE INTO users (id, name, email, password_hash, role) " +
                     "VALUES (?, ?, ?, ?, 'ADMIN')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "seed-admin-001");
            ps.setString(2, "Admin User");
            ps.setString(3, "admin@webblog.com");
            ps.setString(4, PasswordUtils.hash("Admin@123"));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                LOG.info("[SchemaInitializer] Default admin seeded (admin@webblog.com / Admin@123).");
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[SchemaInitializer] Could not seed admin user", e);
        }
    }
}
