package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.DaoUtil;
import org.example.dao.EmployeeAssignmentDao;
import org.example.entity.Building;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingAssignmentService {

    public static void assignBuildingToLeastLoadedEmployee(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Building building1 = DaoUtil.require(session, Building.class, building_id);

                if (building1.getCompany() == null) {
                    throw new IllegalStateException("Building has no company (no contract). Assign is not possible.");
                }

                long company_id = building1.getCompany().getId();
                Employee employee1 = EmployeeAssignmentDao.getLeastLoadedEmployee(session, company_id);

                building1.setEmployee(employee1);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static void contractBuildingToCompany(long building_id, long company_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Building building1 = DaoUtil.require(session, Building.class, building_id);

                if (company_id <= 0) {
                    throw new IllegalArgumentException("company_id must be > 0");
                }

                // Проверяваме дали служителя работи в компанията
                Employee employee1 = EmployeeAssignmentDao.getLeastLoadedEmployee(session, company_id);

                building1.setCompany(employee1.getCompanies().stream()
                        .filter(c -> c.getId() == company_id)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Employee does not work for company_id=" + company_id)));

                building1.setEmployee(employee1);

                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    public static void redistributeBuildingsFromFiredEmployee(long fired_employee_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Employee fired = DaoUtil.require(session, Employee.class, fired_employee_id);

                if (fired.getCompanies() == null || fired.getCompanies().isEmpty()) {
                    throw new IllegalStateException("Fired employee does not work for any company.");
                }

                List<Building> buildings = session.createQuery("""
                    SELECT b FROM Building b
                    WHERE b.employee.id = :empId
                """, Building.class)
                        .setParameter("empId", fired_employee_id)
                        .getResultList();

                // За всяка сграда преразпределяме към служител от същата компания
                for (Building b : buildings) {
                    long company_id = b.getCompany().getId();
                    Employee least = EmployeeAssignmentDao.getLeastLoadedEmployee(session, company_id, fired_employee_id);
                    b.setEmployee(least);
                }

                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }
    }
}
