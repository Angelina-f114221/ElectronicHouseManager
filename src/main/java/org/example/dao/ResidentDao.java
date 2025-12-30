package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Resident;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class ResidentDao {
    public static void createResident(Resident resident) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(resident);
            transaction.commit();
        }
    }
    public static List<Resident> getResidents() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT r FROM Resident r", Resident.class)
                    .getResultList();
        }
    }
    public static Resident getResident(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Resident.class, id);
        }
    }
    public static void updateResident(long id, Resident resident) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Resident resident1 = require(session, Resident.class, id);
                resident1.setName(resident.getName());
                resident1.setAge(resident.getAge());
                resident1.setUses_elevator(resident.isUses_elevator());
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
    public static void deleteResident(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Resident resident1 = require(session, Resident.class, id);
                session.remove(resident1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
