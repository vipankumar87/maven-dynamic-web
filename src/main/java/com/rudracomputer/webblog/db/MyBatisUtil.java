package com.rudracomputer.webblog.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Builds and holds the singleton MyBatis SqlSessionFactory.
 */
public final class MyBatisUtil {

    private static final SqlSessionFactory FACTORY;

    static {
        try (InputStream is = Resources.getResourceAsStream("mybatis-config.xml")) {
            FACTORY = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load mybatis-config.xml", e);
        }
    }

    private MyBatisUtil() {}

    /** Opens a new SqlSession with autoCommit=false (default). */
    public static SqlSession getSession() {
        return FACTORY.openSession();
    }

    /** Opens a new SqlSession with explicit autoCommit control. */
    public static SqlSession getSession(boolean autoCommit) {
        return FACTORY.openSession(autoCommit);
    }
}
