package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Resident;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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
            Resident resident1 = session.find(Resident.class, id);
            if (resident1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Resident with id=" + id + " not found");
            }
            resident1.setName(resident.getName());
            resident1.setAge(resident.getAge());
            resident1.setUses_elevator(resident.isUses_elevator());
            transaction.commit();
        }
    }
    public static void deleteResident(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Resident resident1 = session.find(Resident.class, id);
            session.remove(resident1);
            transaction.commit();
        }
    }
}
