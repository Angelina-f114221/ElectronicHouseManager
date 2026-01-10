package org.example.dao;

import org.hibernate.Session;

public class BillingDao {

    public static long countResidentsOver7UsingElevator(Session session, long apartment_id) {
        if (apartment_id <= 0) {
            throw new IllegalArgumentException("apartment_id must be > 0");
        }
        return session.createQuery("""
            SELECT COUNT(r.id)
            FROM Resident r
            WHERE r.apartment.id = :apartment_id
              AND r.age > 7
              AND r.uses_elevator = true
        """, Long.class)
                .setParameter("apartment_id", apartment_id)
                .getSingleResult();
    }
}
