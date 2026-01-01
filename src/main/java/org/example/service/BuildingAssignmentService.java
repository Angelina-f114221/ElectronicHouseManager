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

    public static void assignBuildingToLeastLoadedEmployee(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Building building1 = DaoUtil.require(session, Building.class, buildingId);

                if (building1.getCompany() == null) {
                    throw new IllegalStateException("Building has no company (no contract). Assign is not possible.");
                }

                long companyId = building1.getCompany().getId();
                Employee employee1 = EmployeeAssignmentDao.getLeastLoadedEmployee(session, companyId);

                building1.setEmployee(employee1);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static void contractBuildingToCompany(long buildingId, long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Building building1 = DaoUtil.require(session, Building.class, buildingId);
                Company company1 = DaoUtil.require(session, Company.class, companyId);

                building1.setCompany(company1);

                Employee employee1 = EmployeeAssignmentDao.getLeastLoadedEmployee(session, companyId);
                building1.setEmployee(employee1);

                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }
    }

    public static void redistributeBuildingsFromFiredEmployee(long firedEmployeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Employee fired = DaoUtil.require(session, Employee.class, firedEmployeeId);

                if (fired.getCompany() == null) {
                    throw new IllegalStateException("Fired employee has no company.");
                }
                long companyId = fired.getCompany().getId();

                List<Building> buildings = session.createQuery("""
                    SELECT b FROM Building b
                    WHERE b.employee.id = :empId
                """, Building.class)
                        .setParameter("empId", firedEmployeeId)
                        .getResultList();

                for (Building b : buildings) {
                    Employee least = EmployeeAssignmentDao.getLeastLoadedEmployee(session, companyId, firedEmployeeId);
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
