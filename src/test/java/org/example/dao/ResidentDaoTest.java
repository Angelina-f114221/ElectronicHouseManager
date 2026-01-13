package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.ResidentDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Resident;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ResidentDaoTest {

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
    void createResident_withElevator_persistsEntity() {
        ResidentDto dto = new ResidentDto(
                0L,
                "Ivan Georgiev",
                LocalDate.of(1990, 3, 15),
                true,
                LocalDate.of(2023, 1, 1),
                apartment1Id
        );

        ResidentDao.createResident(dto);

        List<ResidentDto> all = ResidentDao.getResidents();
        assertTrue(all.stream().anyMatch(r ->
                r.getName().equals("Ivan Georgiev") &&
                        r.getBirth_date().equals(LocalDate.of(1990, 3, 15)) &&
                        r.isUses_elevator() &&
                        r.getContract_start().equals(LocalDate.of(2023, 1, 1)) &&
                        r.getApartment_id() == apartment1Id
        ));
    }


    @Test
    void getResidents_returnsAllResidents() {
        ResidentDto resident1 = new ResidentDto(
                0L, "Resident A", LocalDate.of(1975, 1, 1), true, LocalDate.of(2023, 1, 1), apartment1Id
        );

        ResidentDto resident2 = new ResidentDto(
                0L, "Resident B", LocalDate.of(1978, 5, 15), false, LocalDate.of(2023, 6, 1), apartment2Id
        );

        ResidentDao.createResident(resident1);
        ResidentDao.createResident(resident2);

        List<ResidentDto> all = ResidentDao.getResidents();

        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(r -> r.getName().equals("Resident A")));
        assertTrue(all.stream().anyMatch(r -> r.getName().equals("Resident B")));
    }

    @Test
    void getResident_returnsCorrectDto() {
        long residentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment apt = session.find(Apartment.class, apartment1Id);

            Resident r = new Resident();
            r.setName("Test Resident");
            r.setBirth_date(LocalDate.of(1982, 6, 10));
            r.setUses_elevator(true);
            r.setContract_start(LocalDate.of(2023, 3, 1));
            r.setApartment(apt);
            session.persist(r);

            tx.commit();
            residentId = r.getId();
        }

        ResidentDto dto = ResidentDao.getResident(residentId);

        assertEquals(residentId, dto.getId());
        assertEquals("Test Resident", dto.getName());
        assertEquals(LocalDate.of(1982, 6, 10), dto.getBirth_date());
        assertTrue(dto.isUses_elevator());
        assertEquals(LocalDate.of(2023, 3, 1), dto.getContract_start());
        assertEquals(apartment1Id, dto.getApartment_id());
    }

    @Test
    void updateResident_updatesFields() {
        long residentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment apt = session.find(Apartment.class, apartment1Id);

            Resident r = new Resident();
            r.setName("Original Name");
            r.setBirth_date(LocalDate.of(1980, 1, 1));
            r.setUses_elevator(false);
            r.setContract_start(LocalDate.of(2023, 1, 1));
            r.setApartment(apt);
            session.persist(r);

            tx.commit();
            residentId = r.getId();
        }

        // Актуализираме живущия
        ResidentDto upd = new ResidentDto(
                0L,
                "Updated Name",
                LocalDate.of(1990, 5, 5),
                true,  // Променяме elevator flag
                LocalDate.of(2024, 1, 1),
                apartment2Id
        );

        ResidentDao.updateResident(residentId, upd);

        ResidentDto after = ResidentDao.getResident(residentId);
        assertEquals("Updated Name", after.getName());
        assertEquals(LocalDate.of(1990, 5, 5), after.getBirth_date());
        assertTrue(after.isUses_elevator());
        assertEquals(LocalDate.of(2024, 1, 1), after.getContract_start());
        assertEquals(apartment2Id, after.getApartment_id());
    }

    @Test
    void deleteResident_removesEntity() {
        long residentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment apt = session.find(Apartment.class, apartment1Id);

            Resident r = new Resident();
            r.setName("Resident to Delete");
            r.setBirth_date(LocalDate.of(1980, 1, 1));
            r.setUses_elevator(true);
            r.setContract_start(LocalDate.of(2023, 1, 1));
            r.setApartment(apt);
            session.persist(r);

            tx.commit();
            residentId = r.getId();
        }

        ResidentDao.deleteResident(residentId);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> ResidentDao.getResident(residentId)
        );
    }

}
