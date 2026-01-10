package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.DaoUtil;
import org.example.dao.EmployeeAssignmentDao;
import org.example.entity.Building;
import org.example.entity.Company;
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
                Company company1 = DaoUtil.require(session, Company.class, company_id);

                building1.setCompany(company1);

                Employee employee1 = EmployeeAssignmentDao.getLeastLoadedEmployee(session, company_id);
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

                if (fired.getCompany() == null) {
                    throw new IllegalStateException("Fired employee has no company.");
                }
                long company_id = fired.getCompany().getId();

                List<Building> buildings = session.createQuery("""
                    SELECT b FROM Building b
                    WHERE b.employee.id = :empId
                """, Building.class)
                        .setParameter("empId", fired_employee_id)
                        .getResultList();

                for (Building b : buildings) {
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
