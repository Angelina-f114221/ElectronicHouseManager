package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static org.example.dao.DaoUtil.require;

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
            try {
                Payment payment1 = require(session, Payment.class, id);
                payment1.setAmount(payment.getAmount());
                payment1.setPayment_date(payment.getPayment_date());
                payment1.setPeriod(payment.getPeriod());

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
}
