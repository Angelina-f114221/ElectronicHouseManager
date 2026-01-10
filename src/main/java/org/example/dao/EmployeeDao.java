package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class EmployeeDao {

    public static void createEmployee(EmployeeDto employee) {
        ValidationUtil.validateOrThrow(employee);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Company company = null;
                if (employee.getCompany_id() != null && employee.getCompany_id() > 0) {
                    company = require(session, Company.class, employee.getCompany_id());
                }

                Employee employee1 = new Employee();
                employee1.setName(employee.getName());
                employee1.setAge(employee.getAge());
                employee1.setCompany(company);

                session.persist(employee1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static List<EmployeeDto> getEmployees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.EmployeeDto(
                    e.id,
                    e.name,
                    e.age,
                    c.id
                )
                FROM Employee e
                LEFT JOIN e.company c
            """, EmployeeDto.class).getResultList();
        }
    }

    public static EmployeeDto getEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.EmployeeDto(
                    e.id,
                    e.name,
                    e.age,
                    c.id
                )
                FROM Employee e
                LEFT JOIN e.company c
                WHERE e.id = :id
            """, EmployeeDto.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Employee with id=" + id + " not found"));
        }
    }

    public static void updateEmployee(long id, EmployeeDto employee) {
        ValidationUtil.validateOrThrow(employee);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Employee employee1 = require(session, Employee.class, id);

                Company company = null;
                if (employee.getCompany_id() != null && employee.getCompany_id() > 0) {
                    company = require(session, Company.class, employee.getCompany_id());
                }

                employee1.setName(employee.getName());
                employee1.setAge(employee.getAge());
                employee1.setCompany(company);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static void deleteEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Employee employee1 = require(session, Employee.class, id);
                session.remove(employee1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
