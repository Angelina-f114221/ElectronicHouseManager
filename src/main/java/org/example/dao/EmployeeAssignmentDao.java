package org.example.dao;

import org.example.entity.Employee;
import org.hibernate.Session;

public class EmployeeAssignmentDao {

    public static Employee getLeastLoadedEmployee(Session session, long companyId) {
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
                .getSingleResult();
    }
    public static Employee getLeastLoadedEmployee(Session session, long companyId, long excludeEmployeeId) {
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
                .getSingleResult();
    }
}
