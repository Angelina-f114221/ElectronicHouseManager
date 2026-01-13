package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Fee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class FeeDao {

    public static void createFee(Fee fee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(fee);
                transaction.commit();
            } catch (RuntimeException ex) {
                transaction.rollback();
                throw ex;
            }
        }
    }

    public static List<Fee> getFeesForBuilding(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Fee WHERE building.id = :building_id ORDER BY id DESC",
                    Fee.class
            ).setParameter("building_id", building_id).list();
        }
    }

    public static Fee getFee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Fee.class, id);
        }
    }

    public static List<Fee> getAllFees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Fee ORDER BY id DESC", Fee.class).list();
        }
    }

    public static void updateFee(long id, Fee fee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Fee existing = session.find(Fee.class, id);
                if (existing != null) {
                    existing.setFee_per_sqm(fee.getFee_per_sqm());
                    existing.setFee_per_pet_using_ca(fee.getFee_per_pet_using_ca());
                    existing.setFee_per_person_over_7_using_elevator(fee.getFee_per_person_over_7_using_elevator());
                    // синхронизира промените с базата данни
                    session.merge(existing);
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                transaction.rollback();
                throw ex;
            }
        }
    }

    public static void deleteFee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Fee fee = session.find(Fee.class, id);
                if (fee != null) {
                    session.remove(fee);
                }
                transaction.commit();
            } catch (RuntimeException ex) {
                transaction.rollback();
                throw ex;
            }
        }
    }
}