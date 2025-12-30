package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PaymentDao {
    public static void createPayment(Payment payment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(payment);
            transaction.commit();
        }
    }
    public static List<Payment> getPayments() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT p FROM Payment p", Payment.class)
                    .getResultList();
        }
    }
    public static Payment getPayment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Payment.class, id);
        }
    }
    public static void updatePayment(long id, Payment payment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Payment payment1 = session.find(Payment.class, id);
            if (payment1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Payment with id=" + id + " not found");
            }
            payment1.setAmount(payment.getAmount());
            transaction.commit();
        }
    }
    public static void deletePayment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Payment payment1 = session.find(Payment.class, id);
            if (payment1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Payment with id=" + id + " not found");
            }
            session.remove(payment1);
            transaction.commit();
        }
    }
}
