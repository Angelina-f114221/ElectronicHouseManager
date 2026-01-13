package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoTest {

    private static long company1Id;
    private static long company2Id;

    @BeforeAll
    static void init() {
        // Създаваме две тестови компании
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c1 = new Company();
            c1.setName("Test Company 1");
            session.persist(c1);

            Company c2 = new Company();
            c2.setName("Test Company 2");
            session.persist(c2);

            tx.commit();
            company1Id = c1.getId();
            company2Id = c2.getId();
        }
    }

    @Test
    void createEmployee_withCompanies_persistsEntity() {
        Set<Long> companyIds = new HashSet<>();
        companyIds.add(company1Id);
        companyIds.add(company2Id);

        EmployeeDto dto = new EmployeeDto(
                0L,
                "John Doe",
                LocalDate.of(1990, 5, 15),
                companyIds
        );

        EmployeeDao.createEmployee(dto);

        List<EmployeeDto> all = EmployeeDao.getEmployees();
        assertTrue(all.stream().anyMatch(e ->
                e.getName().equals("John Doe") &&
                        e.getBirth_date().equals(LocalDate.of(1990, 5, 15)) &&
                        e.getCompany_ids().contains(company1Id) &&
                        e.getCompany_ids().contains(company2Id)
        ));
    }

    @Test
    void getEmployees_returnsAllEmployees() {
        Set<Long> ids1 = new HashSet<>();
        ids1.add(company1Id);

        EmployeeDao.createEmployee(new EmployeeDto(
                0L, "Employee A", LocalDate.of(1985, 1, 1), ids1
        ));

        Set<Long> ids2 = new HashSet<>();
        ids2.add(company2Id);

        EmployeeDao.createEmployee(new EmployeeDto(
                0L, "Employee B", LocalDate.of(1988, 3, 15), ids2
        ));

        List<EmployeeDto> all = EmployeeDao.getEmployees();

        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(e -> e.getName().equals("Employee A")));
        assertTrue(all.stream().anyMatch(e -> e.getName().equals("Employee B")));
    }

    @Test
    void getEmployee_returnsCorrectDto() {
        long employeeId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c = session.find(Company.class, company1Id);

            Employee e = new Employee();
            e.setName("Test Employee");
            e.setBirth_date(LocalDate.of(1991, 6, 10));
            e.setCompanies(new HashSet<>());
            e.getCompanies().add(c);
            session.persist(e);

            tx.commit();
            employeeId = e.getId();
        }

        EmployeeDto dto = EmployeeDao.getEmployee(employeeId);

        assertEquals(employeeId, dto.getId());
        assertEquals("Test Employee", dto.getName());
        assertEquals(LocalDate.of(1991, 6, 10), dto.getBirth_date());
        assertTrue(dto.getCompany_ids().contains(company1Id));
    }

    @Test
    void updateEmployee_updatesFields() {
        long employeeId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c = session.find(Company.class, company1Id);  // Вземете компания

            Employee e = new Employee();
            e.setName("Original Name");
            e.setBirth_date(LocalDate.of(1990, 1, 1));
            e.setCompanies(new HashSet<>());
            e.getCompanies().add(c);
            session.persist(e);

            tx.commit();
            employeeId = e.getId();
        }

        // Актуализираме служителя
        Set<Long> newCompanyIds = new HashSet<>();
        newCompanyIds.add(company1Id);

        EmployeeDto upd = new EmployeeDto(
                0L,
                "Updated Name",
                LocalDate.of(1995, 5, 5),
                newCompanyIds
        );

        EmployeeDao.updateEmployee(employeeId, upd);

        EmployeeDto after = EmployeeDao.getEmployee(employeeId);
        assertEquals("Updated Name", after.getName());
        assertEquals(LocalDate.of(1995, 5, 5), after.getBirth_date());
        assertTrue(after.getCompany_ids().contains(company1Id));
    }


    @Test
    void deleteEmployee_removesEntity() {
        long employeeId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c = session.find(Company.class, company1Id);  // Вземете компания

            Employee e = new Employee();
            e.setName("Employee to Delete");
            e.setBirth_date(LocalDate.of(1990, 1, 1));
            e.setCompanies(new HashSet<>());
            e.getCompanies().add(c);
            session.persist(e);

            tx.commit();
            employeeId = e.getId();
        }

        EmployeeDao.deleteEmployee(employeeId);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> EmployeeDao.getEmployee(employeeId)
        );
    }


    @Test
    void employee_futureBirthDate_throwsException() {
        LocalDate futureDate = LocalDate.now().plusYears(1);

        EmployeeDto dto = new EmployeeDto(
                0L,
                "Future Employee",
                futureDate,
                new HashSet<>()
        );

        assertThrows(
                Exception.class,
                () -> EmployeeDao.createEmployee(dto)
        );
    }
}
