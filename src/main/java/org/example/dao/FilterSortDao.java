package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.CompanyIncomeDto;
import org.example.dto.EmployeeBuildingsDto;
import org.example.dto.ResidentShortDto;
import org.hibernate.Session;

import java.util.List;

public class FilterSortDao {

    public static List<CompanyIncomeDto> getCompaniesByIncomeDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.CompanyIncomeDto(
                    c.id,
                    c.name,
                    COALESCE(SUM(p.amount), 0)
                )
                FROM Company c
                LEFT JOIN c.buildings b
                LEFT JOIN b.apartments a
                LEFT JOIN a.payments p
                GROUP BY c.id, c.name
                ORDER BY COALESCE(SUM(p.amount), 0) DESC
            """, CompanyIncomeDto.class).getResultList();
        }
    }

    public static List<EmployeeBuildingsDto> getEmployeesByNameAsc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.EmployeeBuildingsDto(
                    e.id,
                    e.name,
                    COUNT(b.id)
                )
                FROM Employee e
                LEFT JOIN e.buildings b
                GROUP BY e.id, e.name
                ORDER BY e.name ASC
            """, EmployeeBuildingsDto.class).getResultList();
        }
    }

    public static List<EmployeeBuildingsDto> getEmployeesByBuildingsCountDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.EmployeeBuildingsDto(
                    e.id,
                    e.name,
                    COUNT(b.id)
                )
                FROM Employee e
                LEFT JOIN e.buildings b
                GROUP BY e.id, e.name
                ORDER BY COUNT(b.id) DESC
            """, EmployeeBuildingsDto.class).getResultList();
        }
    }

    public static List<ResidentShortDto> getResidentsByNameAsc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.ResidentShortDto(
                    r.id,
                    r.name,
                    r.age
                )
                FROM Resident r
                ORDER BY r.name ASC
            """, ResidentShortDto.class).getResultList();
        }
    }

    public static List<ResidentShortDto> getResidentsByAgeDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.ResidentShortDto(
                    r.id,
                    r.name,
                    r.age
                )
                FROM Resident r
                ORDER BY r.age DESC
            """, ResidentShortDto.class).getResultList();
        }
    }
}
