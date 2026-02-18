package com.rudracomputer.webblog.util;

import com.rudracomputer.webblog.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory user store (demo only).
 * Replace with a database-backed DAO for production.
 */
public final class UserStore {

    private static final UserStore INSTANCE = new UserStore();
    private final Map<String, User> byEmail = new ConcurrentHashMap<>();
    private final Map<String, User> byId    = new ConcurrentHashMap<>();
    private final Map<String, User> byToken = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(10);

    private UserStore() {
        // Seed demo accounts
        addUser(new User("1", "Admin User",  "admin@webblog.com",
                         PasswordUtils.hash("Admin@123"), "ADMIN"));
        addUser(new User("2", "Jane Smith",  "jane@example.com",
                         PasswordUtils.hash("User@123"),  "USER"));
        addUser(new User("3", "Bob Johnson", "bob@example.com",
                         PasswordUtils.hash("User@123"),  "USER"));
    }

    public static UserStore getInstance() { return INSTANCE; }

    // ---- Read ----

    public User findByEmail(String email) {
        return byEmail.get(email.toLowerCase());
    }

    public User findById(String id) {
        return byId.get(id);
    }

    public User findByResetToken(String token) {
        return byToken.get(token);
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>(byId.values());
        list.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        return list;
    }

    public long countByRole(String role) {
        return byId.values().stream().filter(u -> role.equals(u.getRole())).count();
    }

    public long countNewToday() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        return byId.values().stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().isAfter(startOfDay))
                .count();
    }

    // ---- Write ----

    public User createUser(String name, String email, String plainPassword) {
        String id = String.valueOf(idSeq.incrementAndGet());
        User u = new User(id, name, email, PasswordUtils.hash(plainPassword), "USER");
        addUser(u);
        return u;
    }

    public void updatePassword(User user, String newPlainPassword) {
        user.setPasswordHash(PasswordUtils.hash(newPlainPassword));
        clearResetToken(user);
    }

    public void setResetToken(User user, String token) {
        // Remove old token mapping if any
        if (user.getResetToken() != null) {
            byToken.remove(user.getResetToken());
        }
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        byToken.put(token, user);
    }

    public void clearResetToken(User user) {
        if (user.getResetToken() != null) {
            byToken.remove(user.getResetToken());
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
        }
    }

    public void toggleRole(User user) {
        user.setRole("ADMIN".equals(user.getRole()) ? "USER" : "ADMIN");
    }

    public boolean deleteById(String id) {
        User u = byId.remove(id);
        if (u != null) {
            byEmail.remove(u.getEmail().toLowerCase());
            if (u.getResetToken() != null) byToken.remove(u.getResetToken());
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return byEmail.containsKey(email.toLowerCase());
    }

    // ---- Private helper ----

    private void addUser(User u) {
        byId.put(u.getId(), u);
        byEmail.put(u.getEmail().toLowerCase(), u);
    }
}
