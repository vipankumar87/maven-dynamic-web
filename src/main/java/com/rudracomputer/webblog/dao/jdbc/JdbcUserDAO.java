package com.rudracomputer.webblog.dao.jdbc;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.db.DBConnection;
import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.PasswordUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Plain JDBC implementation of {@link UserDAO}.
 * Every method opens a connection, executes SQL, and closes resources via try-with-resources.
 */
public class JdbcUserDAO implements UserDAO {

    // ------------------------------------------------------------------ READ

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByEmail failed", e);
        }
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_token = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("findByResetToken failed", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY created_at ASC";
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed", e);
        }
        return list;
    }

    @Override
    public long countByRole(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("countByRole failed", e);
        }
    }

    @Override
    public long countNewToday() {
        String sql = "SELECT COUNT(*) FROM users WHERE DATE(created_at) = CURDATE()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("countNewToday failed", e);
        }
    }

    // ----------------------------------------------------------------- WRITE

    @Override
    public User create(String name, String email, String plainPassword) {
        String sql = "INSERT INTO users (id, name, email, password_hash, role, created_at) VALUES (?,?,?,?,?,?)";
        String id  = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, email.toLowerCase());
            ps.setString(4, PasswordUtils.hash(plainPassword));
            ps.setString(5, "USER");
            ps.setTimestamp(6, Timestamp.valueOf(now));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("create user failed", e);
        }

        User u = new User(id, name, email.toLowerCase(), PasswordUtils.hash(plainPassword), "USER");
        u.setCreatedAt(now);
        return u;
    }

    @Override
    public void updatePassword(User user, String newPlainPassword) {
        String sql = "UPDATE users SET password_hash = ?, reset_token = NULL, reset_token_expiry = NULL WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, PasswordUtils.hash(newPlainPassword));
            ps.setString(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("updatePassword failed", e);
        }
        user.setPasswordHash(PasswordUtils.hash(newPlainPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
    }

    @Override
    public void setResetToken(User user, String token) {
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);
        String sql = "UPDATE users SET reset_token = ?, reset_token_expiry = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setTimestamp(2, Timestamp.valueOf(expiry));
            ps.setString(3, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("setResetToken failed", e);
        }
        user.setResetToken(token);
        user.setResetTokenExpiry(expiry);
    }

    @Override
    public void clearResetToken(User user) {
        String sql = "UPDATE users SET reset_token = NULL, reset_token_expiry = NULL WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("clearResetToken failed", e);
        }
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
    }

    @Override
    public void toggleRole(User user) {
        String newRole = "ADMIN".equals(user.getRole()) ? "USER" : "ADMIN";
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setString(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("toggleRole failed", e);
        }
        user.setRole(newRole);
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("deleteById failed", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("existsByEmail failed", e);
        }
    }

    // ---------------------------------------------------------- RESULT MAPPER

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getString("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) u.setCreatedAt(created.toLocalDateTime());

        u.setResetToken(rs.getString("reset_token"));

        Timestamp expiry = rs.getTimestamp("reset_token_expiry");
        if (expiry != null) u.setResetTokenExpiry(expiry.toLocalDateTime());

        return u;
    }
}
