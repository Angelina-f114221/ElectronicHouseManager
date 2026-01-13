package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.PaymentDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Payment;
import org.example.entity.PaymentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDaoTest {

    private static long apartmentId;
    private static String paidStatusCode = "PAID";

    @BeforeAll
    static void init() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            PaymentStatus existingStatus = session.createQuery(
                            "FROM PaymentStatus WHERE code = :code",
                            PaymentStatus.class
                    )
                    .setParameter("code", paidStatusCode)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingStatus == null) {
                PaymentStatus status = new PaymentStatus();
                status.setCode(paidStatusCode);
                session.persist(status);
            }

            Building b = new Building();
            b.setName("Test Building");
            b.setFloors(5);
            b.setAddress("Test Address");
            b.setCommon_areas(BigDecimal.valueOf(100));
            b.setTotal_areas(BigDecimal.valueOf(500));
            b.setContract_start_date(LocalDate.now());
            session.persist(b);

            Apartment a = new Apartment();
            a.setBuilding(b);
            a.setNumber(101);
            a.setFloor(1);
            a.setArea(BigDecimal.valueOf(50));
            a.setPets_using_ca(0);
            session.persist(a);

            tx.commit();
            apartmentId = a.getId();
        }
    }

    @Test
    void getPayment_returnsCorrectDto() {
        long paymentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment a = session.find(Apartment.class, apartmentId);
            PaymentStatus status = session.createQuery(
                            "FROM PaymentStatus WHERE code = :code",
                            PaymentStatus.class
                    )
                    .setParameter("code", paidStatusCode)
                    .getSingleResult();

            Payment p = new Payment();
            p.setAmount(BigDecimal.valueOf(150));
            p.setPayment_date(LocalDate.now());
            p.setPeriod("2026-02");
            p.setApartment(a);
            p.setStatus(status);
            session.persist(p);

            tx.commit();
            paymentId = p.getId();
        }

        PaymentDto dto = PaymentDao.getPayment(paymentId);

        assertEquals(paymentId, dto.getId());
        assertEquals(0, dto.getAmount().compareTo(BigDecimal.valueOf(150)));
        assertEquals("2026-02", dto.getPeriod());
        assertEquals(apartmentId, dto.getApartment_id());
        assertEquals(paidStatusCode, dto.getStatus_code());
    }

    @Test
    void updatePayment_updatesFields() {
        long paymentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment a = session.find(Apartment.class, apartmentId);
            PaymentStatus status = session.createQuery(
                            "FROM PaymentStatus WHERE code = :code",
                            PaymentStatus.class
                    )
                    .setParameter("code", paidStatusCode)
                    .getSingleResult();

            Payment p = new Payment();
            p.setAmount(BigDecimal.valueOf(100));
            p.setPayment_date(LocalDate.now());
            p.setPeriod("2026-03");
            p.setApartment(a);
            p.setStatus(status);
            session.persist(p);

            tx.commit();
            paymentId = p.getId();
        }

        PaymentDto upd = new PaymentDto(
                0L,
                BigDecimal.valueOf(250),
                LocalDate.now(),
                "2026-04",
                apartmentId,
                paidStatusCode
        );

        PaymentDao.updatePayment(paymentId, upd);

        PaymentDto after = PaymentDao.getPayment(paymentId);
        assertEquals(0, after.getAmount().compareTo(BigDecimal.valueOf(250)));
        assertEquals("2026-04", after.getPeriod());
        assertEquals(apartmentId, after.getApartment_id());
    }

    @Test
    void deletePayment_removesEntity() {
        long paymentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment a = session.find(Apartment.class, apartmentId);
            PaymentStatus status = session.createQuery(
                            "FROM PaymentStatus WHERE code = :code",
                            PaymentStatus.class
                    )
                    .setParameter("code", paidStatusCode)
                    .getSingleResult();

            Payment p = new Payment();
            p.setAmount(BigDecimal.valueOf(120));
            p.setPayment_date(LocalDate.now());
            p.setPeriod("2026-05");
            p.setApartment(a);
            p.setStatus(status);
            session.persist(p);

            tx.commit();
            paymentId = p.getId();
        }

        PaymentDao.deletePayment(paymentId);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> PaymentDao.getPayment(paymentId)
        );
    }

    @Test
    void getTotalPaid_returnsSumForPeriod() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Apartment a = session.find(Apartment.class, apartmentId);
            PaymentStatus status = session.createQuery(
                            "FROM PaymentStatus WHERE code = :code",
                            PaymentStatus.class
                    )
                    .setParameter("code", paidStatusCode)
                    .getSingleResult();

            Payment p1 = new Payment();
            p1.setAmount(BigDecimal.valueOf(100));
            p1.setPayment_date(LocalDate.now());
            p1.setPeriod("2026-06");
            p1.setApartment(a);
            p1.setStatus(status);
            session.persist(p1);

            Payment p2 = new Payment();
            p2.setAmount(BigDecimal.valueOf(150));
            p2.setPayment_date(LocalDate.now());
            p2.setPeriod("2026-06");
            p2.setApartment(a);
            p2.setStatus(status);
            session.persist(p2);

            tx.commit();
        }

        BigDecimal total = PaymentDao.getTotalPaid(apartmentId, "2026-06");
        assertEquals(0, total.compareTo(BigDecimal.valueOf(250)));
    }
}
