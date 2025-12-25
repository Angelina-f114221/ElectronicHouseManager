package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
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
}
