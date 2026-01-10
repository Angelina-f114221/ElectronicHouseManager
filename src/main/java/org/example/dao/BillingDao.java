package org.example.dao;

import org.example.entity.Resident;
import org.hibernate.Session;

public class BillingDao {

    public static long countResidentsOver7UsingElevator(Session session, long apartment_id) {
        if (apartment_id <= 0) {
            throw new IllegalArgumentException("apartmentId must be > 0");
        }

        return session.createQuery("""
            SELECT r FROM Resident r
            WHERE r.apartment.id = :apartment_id
              AND r.uses_elevator = true
        """, Resident.class)
                .setParameter("apartment_id", apartment_id)
                .getResultStream()
                .filter(r -> r.getAge() > 7)
                .count();
    }
}
