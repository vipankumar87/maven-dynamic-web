package com.rudracomputer.webblog.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Manages the singleton EntityManagerFactory for the "webblog-pu" persistence unit.
 * Call JPAUtil.getEntityManager() to get a new EntityManager for each request/operation,
 * and always close it in a finally block.
 */
public final class JPAUtil {

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("webblog-pu");

    private JPAUtil() {}

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    /** Call on application shutdown (e.g. ServletContextListener). */
    public static void shutdown() {
        if (EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }
}
