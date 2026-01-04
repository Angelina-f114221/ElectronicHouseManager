package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;

public final class DaoUtil {
    private DaoUtil() {}

    public static <T> T require(Session session, Class<T> type, long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(type.getSimpleName() + " id must be > 0");
        }
        T entity = session.find(type, id);
        if (entity == null) {
            throw new EntityNotFoundException(type.getSimpleName() + " with id=" + id + " not found");
        }
        return entity;
    }

}
