package org.example.dao;
// commit 2
import org.hibernate.Session;

public class BillingDao {

    public static long countResidentsOver7UsingElevator(Session session, long apartmentId) {
        return session.createQuery("""
            SELECT COUNT(r.id)
            FROM Resident r
            WHERE r.apartment.id = :apartmentId
              AND r.age > 7
              AND r.uses_elevator = true
        """, Long.class)
                .setParameter("apartmentId", apartmentId)
                .getSingleResult();
    }
}
