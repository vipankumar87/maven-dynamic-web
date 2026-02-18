package com.rudracomputer.webblog.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class User {

    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private String role; // "USER" | "ADMIN"
    private LocalDateTime createdAt;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    public User() {}

    public User(String id, String name, String email, String passwordHash, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // ---- Getters ----

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getResetToken() { return resetToken; }
    public LocalDateTime getResetTokenExpiry() { return resetTokenExpiry; }

    /** Returns createdAt as java.util.Date for JSTL fmt:formatDate */
    public Date getCreatedAtDate() {
        if (createdAt == null) return null;
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    // ---- Setters ----

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }
}
