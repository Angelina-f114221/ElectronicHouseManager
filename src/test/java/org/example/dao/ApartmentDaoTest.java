package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.ApartmentDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentDaoTest {

    private static long buildingId;

    @BeforeAll
    static void init() {
        // Създаваме сграда, която да ползваме във всички тестове
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Building b = new Building();
            b.setName("Test Building");
            b.setFloors(5);
            b.setAddress("Test address");
            b.setCommon_areas(BigDecimal.valueOf(10));
            b.setTotal_areas(BigDecimal.valueOf(100));
            b.setContract_start_date(LocalDate.now());
            session.persist(b);
            tx.commit();
            buildingId = b.getId();
        }
    }

    @Test
    void createApartment_persistsEntity() {
        // създава тестови данни
        ApartmentDto dto = new ApartmentDto(
                0L,
                101,
                1,
                BigDecimal.valueOf(55.5),
                1,
                buildingId
        );
        // изпълнява метода
        ApartmentDao.createApartment(dto);
        // проверява резултата
        List<ApartmentDto> all = ApartmentDao.getApartments();
        assertTrue(all.stream().anyMatch(a ->
                a.getNumber() == 101 &&
                        a.getFloor() == 1 &&
                        a.getArea().compareTo(BigDecimal.valueOf(55.5)) == 0 &&
                        a.getPets_using_ca() == 1 &&
                        a.getBuilding_id() == buildingId
        ));
    }

    @Test
    void getApartment_returnsCorrectDto() {
        // подготвяме апартамент
        long aptId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Building b = session.find(Building.class, buildingId);

            Apartment a = new Apartment();
            a.setNumber(202);
            a.setFloor(2);
            a.setArea(BigDecimal.valueOf(60));
            a.setPets_using_ca(0);
            a.setBuilding(b);
            session.persist(a);
            tx.commit();
            aptId = a.getId();
        }

        ApartmentDto dto = ApartmentDao.getApartment(aptId);

        assertEquals(aptId, dto.getId());
        assertEquals(202, dto.getNumber());
        assertEquals(2, dto.getFloor());
        assertEquals(0, dto.getPets_using_ca());
        assertEquals(buildingId, dto.getBuilding_id());
        assertEquals(0, dto.getArea().compareTo(BigDecimal.valueOf(60)));
    }

    @Test
    void updateApartment_updatesFields() {
        long aptId;
        // създаваме апартамент
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Building b = session.find(Building.class, buildingId);

            Apartment a = new Apartment();
            a.setNumber(303);
            a.setFloor(3);
            a.setArea(BigDecimal.valueOf(70));
            a.setPets_using_ca(2);
            a.setBuilding(b);
            session.persist(a);
            tx.commit();
            aptId = a.getId();
        }

        ApartmentDto upd = new ApartmentDto(
                0L,
                404,
                4,
                BigDecimal.valueOf(80),
                3,
                buildingId
        );

        ApartmentDao.updateApartment(aptId, upd);

        ApartmentDto after = ApartmentDao.getApartment(aptId);
        assertEquals(404, after.getNumber());
        assertEquals(4, after.getFloor());
        assertEquals(3, after.getPets_using_ca());
        assertEquals(buildingId, after.getBuilding_id());
        assertEquals(0, after.getArea().compareTo(BigDecimal.valueOf(80)));
    }

    @Test
    void deleteApartment_removesEntity() {
        long aptId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Building b = session.find(Building.class, buildingId);

            Apartment a = new Apartment();
            a.setNumber(505);
            a.setFloor(5);
            a.setArea(BigDecimal.valueOf(90));
            a.setPets_using_ca(0);
            a.setBuilding(b);
            session.persist(a);
            tx.commit();
            aptId = a.getId();
        }

        ApartmentDao.deleteApartment(aptId);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> ApartmentDao.getApartment(aptId)
        );
    }
}
