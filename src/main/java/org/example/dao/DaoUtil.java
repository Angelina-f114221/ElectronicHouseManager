package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;

public final class DaoUtil {
    private DaoUtil() {}

    public static <T> T require(Session session, Class<T> type, long id) {
        T entity = session.find(type, id);
        if (entity == null) {
            throw new EntityNotFoundException(type.getSimpleName() + " with id=" + id + " not found");
        }
        return entity;
    }
}
