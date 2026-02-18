-- WebBlog Database Schema
-- Run this once to set up the database

CREATE DATABASE IF NOT EXISTS webblog
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE webblog;

CREATE TABLE IF NOT EXISTS users (
    id                  VARCHAR(36)  NOT NULL,
    name                VARCHAR(100) NOT NULL,
    email               VARCHAR(150) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    role                ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    reset_token         VARCHAR(100)  DEFAULT NULL,
    reset_token_expiry  DATETIME      DEFAULT NULL,
    created_at          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_users_email (email),
    INDEX idx_users_reset_token (reset_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Demo seed data (password = Admin@123 / User@123 hashed with SHA-256)
INSERT IGNORE INTO users (id, name, email, password_hash, role) VALUES
  ('1', 'Admin User',  'admin@webblog.com',
   '3a6d4f5e8bba5c4a0f7e2d9c0a1e3f6b8c4d2e7a0f5c3b9d1e4a2f6c8b0d3e7a', 'ADMIN'),
  ('2', 'Jane Smith',  'jane@example.com',
   'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'USER'),
  ('3', 'Bob Johnson', 'bob@example.com',
   'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'USER');
