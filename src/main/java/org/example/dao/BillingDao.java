package org.example.dao;

import org.hibernate.Session;

public class BillingDao {

    public static long countResidentsOver7UsingElevator(Session session, long apartmentId) {
        if (apartmentId <= 0) {
            throw new IllegalArgumentException("apartment_id must be > 0");
        }
        return session.createQuery("""
            SELECT COUNT(r.id)
            FROM Resident r
            WHERE r.apartment.id = :apartmentId
              AND r.age > 7
              AND r.uses_elevator = true
        """, Long.class)
                .setParameter("apartment_id", apartmentId)
                .getSingleResult();
    }
}
