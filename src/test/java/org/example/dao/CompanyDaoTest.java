package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.CompanyDto;
import org.example.entity.Company;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.ValidationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDaoTest {

    @Test
    void createCompany_persistsEntity() {
        CompanyDto dto = new CompanyDto(0L, "Test Company 1");

        CompanyDao.createCompany(dto);

        List<CompanyDto> all = CompanyDao.getCompanies();
        assertTrue(all.stream().anyMatch(c ->
                c.getName().equals("Test Company 1")
        ));
    }


    @Test
    void getCompanies_returnsAllCompanies() {
        // Създаваме няколко компании
        CompanyDao.createCompany(new CompanyDto(0L, "Company A"));
        CompanyDao.createCompany(new CompanyDto(0L, "Company B"));
        CompanyDao.createCompany(new CompanyDto(0L, "Company C"));

        List<CompanyDto> all = CompanyDao.getCompanies();

        assertTrue(all.size() >= 3);
        assertTrue(all.stream().anyMatch(c -> c.getName().equals("Company A")));
        assertTrue(all.stream().anyMatch(c -> c.getName().equals("Company B")));
        assertTrue(all.stream().anyMatch(c -> c.getName().equals("Company C")));
    }

    @Test
    void getCompany_returnsCorrectDto() {
        long companyId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c = new Company();
            c.setName("Test Company");
            session.persist(c);
            tx.commit();
            companyId = c.getId();
        }

        CompanyDto dto = CompanyDao.getCompany(companyId);

        assertEquals(companyId, dto.getId());
        assertEquals("Test Company", dto.getName());
    }

    @Test
    void updateCompany_updatesFields() {
        long companyId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c = new Company();
            c.setName("Original Name");
            session.persist(c);
            tx.commit();
            companyId = c.getId();
        }

        // Актуализираме компанията
        CompanyDto upd = new CompanyDto(0L, "Updated Name");
        CompanyDao.updateCompany(companyId, upd);

        CompanyDto after = CompanyDao.getCompany(companyId);
        assertEquals("Updated Name", after.getName());
    }

    @Test
    void deleteCompany_removesEntity() {
        long companyId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company c = new Company();
            c.setName("Company to Delete");
            session.persist(c);
            tx.commit();
            companyId = c.getId();
        }

        CompanyDao.deleteCompany(companyId);

        assertThrows(
                ResourceNotFoundException.class,
                () -> CompanyDao.getCompany(companyId)
        );
    }

}