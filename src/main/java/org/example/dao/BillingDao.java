package org.example.dao;

import org.example.entity.Fee;
import org.hibernate.Session;

public class BillingDao {

    /**
     * Count residents over 7 years old using elevator in an apartment
     */
    public static long countResidentsOver7UsingElevator(Session session, long apartment_id) {
        return session.createQuery("""
                SELECT COUNT(r) 
                FROM Resident r 
                WHERE r.apartment.id = :apartmentId 
                AND r.uses_elevator = true
                AND YEAR(CURRENT_DATE) - YEAR(r.birth_date) > 7
            """, Long.class)
                .setParameter("apartmentId", apartment_id)
                .getSingleResult();
    }

    /**
     * Get the latest (most recent) fee for a building
     */
    public static Fee getLatestFeeForBuilding(Session session, long building_id) {
        return session.createQuery("""
                SELECT f FROM Fee f 
                WHERE f.building.id = :buildingId 
                ORDER BY f.start_date DESC
            """, Fee.class)
                .setParameter("buildingId", building_id)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}