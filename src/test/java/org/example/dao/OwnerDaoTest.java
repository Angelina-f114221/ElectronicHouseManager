package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.OwnerDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerDaoTest {

    private static long building1Id;
    private static long apartment1Id;
    private static long apartment2Id;

    @BeforeAll
    static void init() {
        // Създаваме тестова сграда и апартаменти
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Building b = new Building();
            b.setName("Test Building");
            b.setFloors(5);
            b.setAddress("Test Address");
            b.setCommon_areas(BigDecimal.valueOf(100));
            b.setTotal_areas(BigDecimal.valueOf(500));
            b.setContract_start_date(LocalDate.now());
            session.persist(b);

            Apartment apt1 = new Apartment();
            apt1.setNumber(101);
            apt1.setFloor(1);
            apt1.setArea(BigDecimal.valueOf(50));
            apt1.setPets_using_ca(0);
            apt1.setBuilding(b);
            session.persist(apt1);

            Apartment apt2 = new Apartment();
            apt2.setNumber(102);
            apt2.setFloor(1);
            apt2.setArea(BigDecimal.valueOf(55));
            apt2.setPets_using_ca(1);
            apt2.setBuilding(b);
            session.persist(apt2);

            tx.commit();
            building1Id = b.getId();
            apartment1Id = apt1.getId();
            apartment2Id = apt2.getId();
        }
    }

    @Test
    void createOwner_withApartments_persistsEntity() {
        Set<Long> apartmentIds = new HashSet<>();
        apartmentIds.add(apartment1Id);
        apartmentIds.add(apartment2Id);

        OwnerDto dto = new OwnerDto(
                0L,
                "Ivan Petrov",
                LocalDate.of(1980, 3, 15),
                apartmentIds
        );

        OwnerDao.createOwner(dto);

        List<OwnerDto> all = OwnerDao.getOwners();
        assertTrue(all.stream().anyMatch(o ->
                o.getName().equals("Ivan Petrov") &&
                        o.getBirth_date().equals(LocalDate.of(1980, 3, 15)) &&
                        o.getApartment_ids().contains(apartment1Id) &&
                        o.getApartment_ids().contains(apartment2Id)
        ));
    }

    @Test
    void getOwners_returnsAllOwners() {
        Set<Long> ids1 = new HashSet<>();
        ids1.add(apartment1Id);

        OwnerDao.createOwner(new OwnerDto(
                0L, "Owner A", LocalDate.of(1975, 1, 1), ids1
        ));

        Set<Long> ids2 = new HashSet<>();
        ids2.add(apartment2Id);

        OwnerDao.createOwner(new OwnerDto(
                0L, "Owner B", LocalDate.of(1978, 5, 15), ids2
        ));

        List<OwnerDto> all = OwnerDao.getOwners();

        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(o -> o.getName().equals("Owner A")));
        assertTrue(all.stream().anyMatch(o -> o.getName().equals("Owner B")));
    }

    @Test
    void getOwner_returnsCorrectDto() {
        long ownerId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment apt = session.find(Apartment.class, apartment1Id);

            Owner o = new Owner();
            o.setName("Test Owner");
            o.setBirth_date(LocalDate.of(1982, 6, 10));
            o.setApartments(new HashSet<>());
            o.getApartments().add(apt);
            session.persist(o);

            tx.commit();
            ownerId = o.getId();
        }

        OwnerDto dto = OwnerDao.getOwner(ownerId);

        assertEquals(ownerId, dto.getId());
        assertEquals("Test Owner", dto.getName());
        assertEquals(LocalDate.of(1982, 6, 10), dto.getBirth_date());
        assertTrue(dto.getApartment_ids().contains(apartment1Id));
    }

    @Test
    void updateOwner_updatesFields() {
        long ownerId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Owner o = new Owner();
            o.setName("Original Name");
            o.setBirth_date(LocalDate.of(1980, 1, 1));
            o.setApartments(new HashSet<>());
            session.persist(o);

            tx.commit();
            ownerId = o.getId();
        }

        // Актуализираме собственика
        Set<Long> newApartmentIds = new HashSet<>();
        newApartmentIds.add(apartment1Id);

        OwnerDto upd = new OwnerDto(
                0L,
                "Updated Name",
                LocalDate.of(1990, 5, 5),
                newApartmentIds
        );

        OwnerDao.updateOwner(ownerId, upd);

        OwnerDto after = OwnerDao.getOwner(ownerId);
        assertEquals("Updated Name", after.getName());
        assertEquals(LocalDate.of(1990, 5, 5), after.getBirth_date());
        assertTrue(after.getApartment_ids().contains(apartment1Id));
    }

    @Test
    void deleteOwner_removesEntity() {
        long ownerId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Owner o = new Owner();
            o.setName("Owner to Delete");
            o.setBirth_date(LocalDate.of(1980, 1, 1));
            o.setApartments(new HashSet<>());
            session.persist(o);

            tx.commit();
            ownerId = o.getId();
        }

        OwnerDao.deleteOwner(ownerId);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> OwnerDao.getOwner(ownerId)
        );
    }


}
