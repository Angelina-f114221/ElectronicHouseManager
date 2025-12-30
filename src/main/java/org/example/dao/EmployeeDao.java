package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDao {
    public static void createEmployee(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        }
    }
    public static List<Employee> getEmployees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e", Employee.class)
                    .getResultList();
        }
    }
    public static Employee getEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }
    /*
    резултатът трябва да е Employee dto. искам да не получавам всичките свойства нa служителя, a само тези, които в DTO модела са описани. трябва да съпоставя DTO модела с Employee. на мястото на резултата, който искам да получавам, като използвам DTO пиша оператора new и след това описвам DTO-то да бъде fully qualified. Тоест казвам, че DTO моделът се намира в organization example package. след което трябва да кажа, че искам да ползвам employee DTO клас. Важно е като се хитне базата през ентити модела, да може да се съпостави това, което в таблицата на служителя под име има на DTO модел. Тоест трябва да се направи съпоставка между ентити модела и DTO модела. това става през конструктора и alias на entity модела. Значи това е alias за employee entity модела, след което има е.ID, след което е.name. имам и е.position.
     */
    public static List<EmployeeDto> getEmployeesDto(){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.example.dto.EmployeeDto(e.id, e.name, e.age)" +
                    " FROM Employee e", EmployeeDto.class).getResultList();
        }
    }
    public static void updateEmployee(long id, Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee1 = session.find(Employee.class, id);
            if  (employee1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Employee with id=" + id + " not found");
            }
            employee1.setName(employee.getName());
            employee1.setAge(employee.getAge());
            transaction.commit();
        }
    }
    public static void deleteEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee1 = session.find(Employee.class, id);
            if (employee1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Employee with id=" + id + " not found");
            }
            session.remove(employee1);
            transaction.commit();
        }
    }
}
