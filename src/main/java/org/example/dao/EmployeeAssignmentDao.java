package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.entity.Employee;
import org.hibernate.Session;

public class EmployeeAssignmentDao {

    public static Employee getLeastLoadedEmployee(Session session, long company_id) {
        if (company_id <= 0) {
            throw new IllegalArgumentException("company_id must be > 0");
        }
        return session.createQuery("""
            SELECT e
            FROM Employee e
            LEFT JOIN e.companies c
            LEFT JOIN e.buildings b
            WHERE :company_id MEMBER OF e.companies
            GROUP BY e
            ORDER BY COUNT(b.id) ASC, e.id ASC
        """, Employee.class)
                .setParameter("company_id", company_id)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No employees found for company_id=" + company_id));
    }

    public static Employee getLeastLoadedEmployee(Session session, long company_id, long excludeEmployeeId) {
        if (company_id <= 0) {
            throw new IllegalArgumentException("company_id must be > 0");
        }
        if (excludeEmployeeId <= 0) {
            throw new IllegalArgumentException("excludeEmployeeId must be > 0");
        }
        return session.createQuery("""
            SELECT e
            FROM Employee e
            LEFT JOIN e.companies c
            LEFT JOIN e.buildings b
            WHERE :company_id MEMBER OF e.companies
              AND e.id <> :excludeEmployeeId
            GROUP BY e
            ORDER BY COUNT(b.id) ASC, e.id ASC
        """, Employee.class)
                .setParameter("company_id", company_id)
                .setParameter("excludeEmployeeId", excludeEmployeeId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No employees found for company_id=" + company_id
                ));
    }
}
