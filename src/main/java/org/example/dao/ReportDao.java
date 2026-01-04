package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.AmountByKeyDto;
import org.example.dto.IdNameDto;
import org.hibernate.Session;

import java.util.List;

public class ReportDao {

    public static List<IdNameDto> getBuildingsByEmployeeInCompany(long companyId, long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.IdNameDto(b.id, b.name)
                FROM Building b
                WHERE b.company.id = :companyId AND b.employee.id = :employeeId
            """, IdNameDto.class)
                    .setParameter("companyId", companyId)
                    .setParameter("employeeId", employeeId)
                    .getResultList();
        }
    }

    public static long countBuildingsByEmployeeInCompany(long companyId, long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT COUNT(b.id)
                FROM Building b
                WHERE b.company.id = :companyId AND b.employee.id = :employeeId
            """, Long.class)
                    .setParameter("companyId", companyId)
                    .setParameter("employeeId", employeeId)
                    .getSingleResult();
        }
    }

    public static List<Long> getApartmentIdsInBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.id = :buildingId
            """, Long.class)
                    .setParameter("buildingId", buildingId)
                    .getResultList();
        }
    }

    public static long countApartmentsInBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT COUNT(a.id)
                FROM Apartment a
                WHERE a.building.id = :buildingId
            """, Long.class)
                    .setParameter("buildingId", buildingId)
                    .getSingleResult();
        }
    }

    public static List<IdNameDto> getResidentsInBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.IdNameDto(r.id, r.name)
                FROM Resident r
                WHERE r.apartment.building.id = :buildingId
            """, IdNameDto.class)
                    .setParameter("buildingId", buildingId)
                    .getResultList();
        }
    }

    public static long countResidentsInBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT COUNT(r.id)
                FROM Resident r
                WHERE r.apartment.building.id = :buildingId
            """, Long.class)
                    .setParameter("buildingId", buildingId)
                    .getSingleResult();
        }
    }

    public static AmountByKeyDto getPaidSumForCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.AmountByKeyDto(
                    c.id,
                    c.name,
                    COALESCE(SUM(p.amount), 0)
                )
                FROM Company c
                LEFT JOIN c.buildings b
                LEFT JOIN b.apartments a
                LEFT JOIN a.payments p
                WHERE c.id = :companyId
                GROUP BY c.id, c.name
            """, AmountByKeyDto.class)
                    .setParameter("companyId", companyId)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("... not found"));

        }
    }

    public static AmountByKeyDto getPaidSumForBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.AmountByKeyDto(
                    b.id,
                    b.name,
                    COALESCE(SUM(p.amount), 0.0)
                )
                FROM Building b
                LEFT JOIN b.apartments a
                LEFT JOIN a.payments p
                WHERE b.id = :buildingId
                GROUP BY b.id, b.name
            """, AmountByKeyDto.class)
                    .setParameter("buildingId", buildingId)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("... not found"));

        }
    }

    public static AmountByKeyDto getPaidSumForEmployee(long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.AmountByKeyDto(
                    e.id,
                    e.name,
                    COALESCE(SUM(p.amount), 0.0)
                )
                FROM Employee e
                LEFT JOIN e.buildings b
                LEFT JOIN b.apartments a
                LEFT JOIN a.payments p
                WHERE e.id = :employeeId
                GROUP BY e.id, e.name
            """, AmountByKeyDto.class)
                    .setParameter("employeeId", employeeId)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("... not found"));

        }
    }
}
