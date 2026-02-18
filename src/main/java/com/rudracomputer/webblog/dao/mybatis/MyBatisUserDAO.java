package com.rudracomputer.webblog.dao.mybatis;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.db.MyBatisUtil;
import com.rudracomputer.webblog.mapper.UserMapper;
import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.PasswordUtils;

import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MyBatis implementation of {@link UserDAO}.
 *
 * Read operations use autoCommit=true (no explicit transaction needed).
 * Write operations open a session, execute, commit, then close.
 */
public class MyBatisUserDAO implements UserDAO {

    // ------------------------------------------------------------------ READ

    @Override
    public Optional<User> findById(String id) {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return Optional.ofNullable(session.getMapper(UserMapper.class).findById(id));
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return Optional.ofNullable(
                    session.getMapper(UserMapper.class).findByEmail(email.toLowerCase()));
        }
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return Optional.ofNullable(
                    session.getMapper(UserMapper.class).findByResetToken(token));
        }
    }

    @Override
    public List<User> findAll() {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return session.getMapper(UserMapper.class).findAll();
        }
    }

    @Override
    public long countByRole(String role) {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return session.getMapper(UserMapper.class).countByRole(role);
        }
    }

    @Override
    public long countNewToday() {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return session.getMapper(UserMapper.class).countNewToday();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (SqlSession session = MyBatisUtil.getSession(true)) {
            return session.getMapper(UserMapper.class).existsByEmail(email.toLowerCase()) > 0;
        }
    }

    // ----------------------------------------------------------------- WRITE

    @Override
    public User create(String name, String email, String plainPassword) {
        User user = new User(
                UUID.randomUUID().toString(),
                name,
                email.toLowerCase(),
                PasswordUtils.hash(plainPassword),
                "USER"
        );
        try (SqlSession session = MyBatisUtil.getSession()) {
            session.getMapper(UserMapper.class).insert(user);
            session.commit();
        }
        return user;
    }

    @Override
    public void updatePassword(User user, String newPlainPassword) {
        String hash = PasswordUtils.hash(newPlainPassword);
        try (SqlSession session = MyBatisUtil.getSession()) {
            session.getMapper(UserMapper.class).updatePassword(user.getId(), hash);
            session.commit();
        }
        user.setPasswordHash(hash);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
    }

    @Override
    public void setResetToken(User user, String token) {
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);
        try (SqlSession session = MyBatisUtil.getSession()) {
            session.getMapper(UserMapper.class).setResetToken(user.getId(), token, expiry);
            session.commit();
        }
        user.setResetToken(token);
        user.setResetTokenExpiry(expiry);
    }

    @Override
    public void clearResetToken(User user) {
        try (SqlSession session = MyBatisUtil.getSession()) {
            session.getMapper(UserMapper.class).clearResetToken(user.getId());
            session.commit();
        }
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
    }

    @Override
    public void toggleRole(User user) {
        String newRole = "ADMIN".equals(user.getRole()) ? "USER" : "ADMIN";
        try (SqlSession session = MyBatisUtil.getSession()) {
            session.getMapper(UserMapper.class).updateRole(user.getId(), newRole);
            session.commit();
        }
        user.setRole(newRole);
    }

    @Override
    public boolean deleteById(String id) {
        try (SqlSession session = MyBatisUtil.getSession()) {
            int rows = session.getMapper(UserMapper.class).deleteById(id);
            session.commit();
            return rows > 0;
        }
    }
}
