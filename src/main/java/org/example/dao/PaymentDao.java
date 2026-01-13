package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.PaymentDto;
import org.example.entity.Apartment;
import org.example.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;
import static org.example.dao.DaoUtil.require;

public class PaymentDao {

    public static void createPayment(PaymentDto payment) {
        ValidationUtil.validateOrThrow(payment);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Apartment apartment1 = require(session, Apartment.class, payment.getApartment_id());

                Payment payment1 = new Payment();
                payment1.setAmount(payment.getAmount());
                payment1.setPayment_date(payment.getPayment_date());
                payment1.setPeriod(payment.getPeriod());
                payment1.setApartment(apartment1);


                session.persist(payment1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static List<PaymentDto> getPayments() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.PaymentDto(
                    p.id,
                    p.amount,
                    p.payment_date,
                    p.period,
                    p.apartment.id,
                    p.status.code
                )
                FROM Payment p
            """, PaymentDto.class).getResultList();
        }
    }

    public static PaymentDto getPayment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.PaymentDto(
                    p.id,
                    p.amount,
                    p.payment_date,
                    p.period,
                    p.apartment.id,
                    p.status.code
                )
                FROM Payment p
                WHERE p.id = :id
            """, PaymentDto.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Payment with id=" + id + " not found"));
        }
    }

    public static void updatePayment(long id, PaymentDto payment) {
        ValidationUtil.validateOrThrow(payment);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Payment payment1 = require(session, Payment.class, id);
                Apartment apartment1 = require(session, Apartment.class, payment.getApartment_id());

                payment1.setAmount(payment.getAmount());
                payment1.setPayment_date(payment.getPayment_date());
                payment1.setPeriod(payment.getPeriod());
                payment1.setApartment(apartment1);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static void deletePayment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Payment payment1 = require(session, Payment.class, id);
                session.remove(payment1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
    public static BigDecimal getTotalPaid(long apartment_id, String period) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            // търси сума на всички плащания на апартамент за даден месец
            return session.createQuery(
                            "SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p " +
                                    "WHERE p.apartment.id = :apt_id AND p.period = :period",
                            BigDecimal.class
                    )
                    .setParameter("apt_id", apartment_id)
                    .setParameter("period", period)
                    .getSingleResult();
        }
    }
}