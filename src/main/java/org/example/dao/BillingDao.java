package org.example.dao;
//
import org.example.entity.Fee;
import org.hibernate.Session;

public class BillingDao {

    public static long countResidentsOver7UsingElevator(Session session, long apartment_id) {
        return session.createQuery("""
                SELECT COUNT(r) 
                FROM Resident r 
                WHERE r.apartment.id = :apartment_id 
                AND r.uses_elevator = true
                AND YEAR(CURRENT_DATE) - YEAR(r.birth_date) > 7
            """, Long.class)
                .setParameter("apartment_id", apartment_id)
                .getSingleResult();
    }
    // от списъка се взема първата стойност, която отговаря на най-голяатата/скорошна дата (descending)
    public static Fee getLatestFeeForBuilding(Session session, long building_id) {
        return session.createQuery("""
                SELECT f FROM Fee f 
                WHERE f.building.id = :building_id 
                ORDER BY f.start_date DESC
            """, Fee.class)
                .setParameter("building_id", building_id)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}