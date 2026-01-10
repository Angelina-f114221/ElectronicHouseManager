package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.dao.DaoUtil.require;

public class EmployeeDao {

    public static void createEmployee(EmployeeDto employee) {
        ValidationUtil.validateOrThrow(employee);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Set<Company> companies = new HashSet<>();
                if (employee.getCompany_ids() != null && !employee.getCompany_ids().isEmpty()) {
                    for (Long companyId : employee.getCompany_ids()) {
                        Company company = require(session, Company.class, companyId);
                        companies.add(company);
                    }
                }

                Employee employee1 = new Employee();
                employee1.setName(employee.getName());
                employee1.setBirth_date(employee.getBirth_date());
                employee1.setCompanies(companies);

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
            List<Employee> employees = session.createQuery(
                    """
                    FROM Employee e
                    LEFT JOIN FETCH e.companies
                    """, Employee.class).getResultList();

            return employees.stream()
                    .map(e -> new EmployeeDto(
                            e.getId(),
                            e.getName(),
                            e.getBirth_date(),
                            e.getCompanies().stream().map(Company::getId).collect(java.util.stream.Collectors.toSet())
                    ))
                    .toList();
        }
    }

    public static EmployeeDto getEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Employee employee = session.createQuery(
                            """
                            FROM Employee e
                            LEFT JOIN FETCH e.companies
                            WHERE e.id = :id
                            """, Employee.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Employee with id=" + id + " not found"));

            return new EmployeeDto(
                    employee.getId(),
                    employee.getName(),
                    employee.getBirth_date(),
                    employee.getCompanies().stream().map(Company::getId).collect(java.util.stream.Collectors.toSet())
            );
        }
    }

    public static void updateEmployee(long id, EmployeeDto employee) {
        ValidationUtil.validateOrThrow(employee);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Employee employee1 = require(session, Employee.class, id);

                Set<Company> companies = new HashSet<>();
                if (employee.getCompany_ids() != null && !employee.getCompany_ids().isEmpty()) {
                    for (Long companyId : employee.getCompany_ids()) {
                        Company company = require(session, Company.class, companyId);
                        companies.add(company);
                    }
                }

                employee1.setName(employee.getName());
                employee1.setBirth_date(employee.getBirth_date());
                employee1.setCompanies(companies);

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
