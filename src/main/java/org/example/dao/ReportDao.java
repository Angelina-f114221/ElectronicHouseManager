package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.AmountByKeyDto;
import org.example.dto.IdNameDto;
import org.hibernate.Session;

import java.util.List;

public class ReportDao {

    public static List<IdNameDto> getBuildingsByEmployeeInCompany(long company_id, long employee_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.IdNameDto(b.id, b.name)
                FROM Building b
                WHERE b.company.id = :company_id AND b.employee.id = :employee_id
            """, IdNameDto.class)
                    .setParameter("company_id", company_id)
                    .setParameter("employee_id", employee_id)
                    .getResultList();
        }
    }

    public static long countBuildingsByEmployeeInCompany(long company_id, long employee_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT COUNT(b.id)
                FROM Building b
                WHERE b.company.id = :company_id AND b.employee.id = :employee_id
            """, Long.class)
                    .setParameter("company_id", company_id)
                    .setParameter("employee_id", employee_id)
                    .getSingleResult();
        }
    }

    public static List<Long> getApartmentIdsInBuilding(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.id = :building_id
            """, Long.class)
                    .setParameter("building_id", building_id)
                    .getResultList();
        }
    }

    public static long countApartmentsInBuilding(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT COUNT(a.id)
                FROM Apartment a
                WHERE a.building.id = :building_id
            """, Long.class)
                    .setParameter("building_id", building_id)
                    .getSingleResult();
        }
    }

    public static List<IdNameDto> getResidentsInBuilding(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.IdNameDto(r.id, r.name)
                FROM Resident r
                WHERE r.apartment.building.id = :building_id
            """, IdNameDto.class)
                    .setParameter("building_id", building_id)
                    .getResultList();
        }
    }

    public static long countResidentsInBuilding(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT COUNT(r.id)
                FROM Resident r
                WHERE r.apartment.building.id = :building_id
            """, Long.class)
                    .setParameter("building_id", building_id)
                    .getSingleResult();
        }
    }

    public static AmountByKeyDto getPaidSumForCompany(long company_id) {
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
                WHERE c.id = :company_id
                GROUP BY c.id, c.name
            """, AmountByKeyDto.class)
                    .setParameter("company_id", company_id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("... not found"));

        }
    }

    public static AmountByKeyDto getPaidSumForBuilding(long building_id) {
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
                WHERE b.id = :building_id
                GROUP BY b.id, b.name
            """, AmountByKeyDto.class)
                    .setParameter("building_id", building_id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("... not found"));

        }
    }

    public static AmountByKeyDto getPaidSumForEmployee(long employee_id) {
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
                WHERE e.id = :employee_id
                GROUP BY e.id, e.name
            """, AmountByKeyDto.class)
                    .setParameter("employee_id", employee_id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("... not found"));

        }
    }
}
