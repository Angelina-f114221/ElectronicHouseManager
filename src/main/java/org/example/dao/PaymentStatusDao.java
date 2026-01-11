package org.example.dao;

import org.example.entity.PaymentStatus;
import org.example.configuration.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class PaymentStatusDao {
    public static void createPaymentStatus(PaymentStatus status) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(status);
                transaction.commit();
            } catch (RuntimeException ex) {
                transaction.rollback();
                throw ex;
            }
        }
    }
    public static List<PaymentStatus> getPaymentStatuses() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM PaymentStatus", PaymentStatus.class).list();
        }
    }

    public static PaymentStatus getPaymentStatus(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(PaymentStatus.class, id);
        }
    }

    public static PaymentStatus getByCode(String code) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM PaymentStatus WHERE code = :code",
                    PaymentStatus.class
            ).setParameter("code", code).getSingleResult();
        }
    }
}
