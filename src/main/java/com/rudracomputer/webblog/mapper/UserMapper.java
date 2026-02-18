package com.rudracomputer.webblog.mapper;

import com.rudracomputer.webblog.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis mapper interface for User SQL operations.
 * SQL statements are defined in UserMapper.xml.
 */
public interface UserMapper {

    User findById(String id);

    User findByEmail(String email);

    User findByResetToken(String token);

    List<User> findAll();

    long countByRole(String role);

    long countNewToday();

    long existsByEmail(String email);

    int insert(User user);

    int updatePassword(@Param("id") String id, @Param("passwordHash") String passwordHash);

    int setResetToken(@Param("id") String id,
                      @Param("resetToken") String token,
                      @Param("resetTokenExpiry") java.time.LocalDateTime expiry);

    int clearResetToken(String id);

    int updateRole(@Param("id") String id, @Param("role") String role);

    int deleteById(String id);
}
