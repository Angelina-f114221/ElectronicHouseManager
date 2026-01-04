package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.entity.Employee;
import org.hibernate.Session;

public class EmployeeAssignmentDao {

    public static Employee getLeastLoadedEmployee(Session session, long companyId) {
        if (companyId <= 0) {
            throw new IllegalArgumentException("companyId must be > 0");
        }
        return session.createQuery("""
        SELECT e
        FROM Employee e
        LEFT JOIN e.buildings b
        WHERE e.company.id = :companyId
        GROUP BY e
        ORDER BY COUNT(b.id) ASC, e.id ASC
    """, Employee.class)
                .setParameter("companyId", companyId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No employees found for companyId=" + companyId));

    }
    public static Employee getLeastLoadedEmployee(Session session, long companyId, long excludeEmployeeId) {
        if (companyId <= 0) {
            throw new IllegalArgumentException("companyId must be > 0");
        }
        if (excludeEmployeeId <= 0) {
            throw new IllegalArgumentException("excludeEmployeeId must be > 0");
        }
        return session.createQuery("""
            SELECT e
            FROM Employee e
            LEFT JOIN e.buildings b
            WHERE e.company.id = :companyId
              AND e.id <> :excludeEmployeeId
            GROUP BY e
            ORDER BY COUNT(b.id) ASC, e.id ASC
        """, Employee.class)
                .setParameter("companyId", companyId)
                .setParameter("excludeEmployeeId", excludeEmployeeId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No employees found for companyId=" + companyId
                ));
    }
}
