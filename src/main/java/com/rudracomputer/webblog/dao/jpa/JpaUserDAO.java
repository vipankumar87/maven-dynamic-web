package com.rudracomputer.webblog.dao.jpa;

import com.rudracomputer.webblog.dao.UserDAO;
import com.rudracomputer.webblog.db.JPAUtil;
import com.rudracomputer.webblog.model.User;
import com.rudracomputer.webblog.util.PasswordUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA / Hibernate implementation of {@link UserDAO}.
 *
 * Each method gets its own EntityManager and manages its own transaction.
 * This is the typical pattern for plain Tomcat (RESOURCE_LOCAL).
 */
public class JpaUserDAO implements UserDAO {

    // ------------------------------------------------------------------ READ

    @Override
    public Optional<User> findById(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(User.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email.toLowerCase());
            return q.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.resetToken = :token", User.class);
            q.setParameter("token", token);
            return q.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM User u ORDER BY u.createdAt ASC", User.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByRole(String role) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.role = :role", Long.class)
                    .setParameter("role", role)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countNewToday() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            return em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.createdAt >= :start", Long.class)
                    .setParameter("start", startOfDay)
                    .getSingleResult();
        } finally {
            em.close();
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
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("create user failed", e);
        } finally {
            em.close();
        }
        return user;
    }

    @Override
    public void updatePassword(User user, String newPlainPassword) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managed = em.find(User.class, user.getId());
            managed.setPasswordHash(PasswordUtils.hash(newPlainPassword));
            managed.setResetToken(null);
            managed.setResetTokenExpiry(null);
            tx.commit();
            // Sync detached object
            user.setPasswordHash(managed.getPasswordHash());
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("updatePassword failed", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void setResetToken(User user, String token) {
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managed = em.find(User.class, user.getId());
            managed.setResetToken(token);
            managed.setResetTokenExpiry(expiry);
            tx.commit();
            user.setResetToken(token);
            user.setResetTokenExpiry(expiry);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("setResetToken failed", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void clearResetToken(User user) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managed = em.find(User.class, user.getId());
            managed.setResetToken(null);
            managed.setResetTokenExpiry(null);
            tx.commit();
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("clearResetToken failed", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void toggleRole(User user) {
        String newRole = "ADMIN".equals(user.getRole()) ? "USER" : "ADMIN";
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managed = em.find(User.class, user.getId());
            managed.setRole(newRole);
            tx.commit();
            user.setRole(newRole);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("toggleRole failed", e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteById(String id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managed = em.find(User.class, id);
            if (managed == null) {
                tx.rollback();
                return false;
            }
            em.remove(managed);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("deleteById failed", e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                    .setParameter("email", email.toLowerCase())
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}
