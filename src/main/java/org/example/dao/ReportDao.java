package org.example.dao;
// not seen

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.AmountByKeyDto;
import org.example.dto.IdNameDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Fee;
import org.example.entity.Payment;
import org.example.exception.BillingException;
import org.example.exception.ResourceNotFoundException;
import org.hibernate.Session;

import java.math.BigDecimal;
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
                    .orElseThrow(() -> new EntityNotFoundException("Company not found"));
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
                    .orElseThrow(() -> new EntityNotFoundException("Building not found"));
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
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        }
    }

    public static BigDecimal getDueSumForCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            Company company = session.find(Company.class, companyId);
            if (company == null) {
                throw new ResourceNotFoundException("Company", companyId);
            }

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
            Root<Company> companyRoot = cq.from(Company.class);
            Join<Company, Building> buildings = companyRoot.join("buildings");
            Join<Building, Apartment> apartments = buildings.join("apartments");
            Join<Apartment, Fee> fees = apartments.join("fees");

            cq.select(cb.sum(fees.get("feeSqm")));
            cq.where(cb.equal(companyRoot.get("id"), companyId));

            BigDecimal result = session.createQuery(cq).getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BillingException("Failed to calculate due sum for company " + companyId, e);
        }
    }


    public static BigDecimal getDueSumForBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
            Root<Building> building = cq.from(Building.class);
            Join<Building, Apartment> apartments = building.join("apartments");
            Join<Apartment, Fee> fees = apartments.join("fees");

            cq.select(cb.sum(fees.get("feeSqm")));
            cq.where(cb.equal(building.get("id"), buildingId));
            cq.groupBy(building.get("id"));

            BigDecimal result = session.createQuery(cq).getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }
}
