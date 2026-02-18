package com.rudracomputer.webblog.dao;

import com.rudracomputer.webblog.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Common DAO contract — shared by all DB implementations (JDBC, JPA, MyBatis).
 */
public interface UserDAO {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);

    List<User> findAll();

    long countByRole(String role);

    long countNewToday();

    User create(String name, String email, String plainPassword);

    void updatePassword(User user, String newPlainPassword);

    void setResetToken(User user, String token);

    void clearResetToken(User user);

    void toggleRole(User user);

    boolean deleteById(String id);

    boolean existsByEmail(String email);
}
