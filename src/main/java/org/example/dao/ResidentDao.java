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
    public static Resident getResident(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Resident.class, id);
        }
    }
    public static void updateResident(long id, Resident resident) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Resident resident1 = session.find(Resident.class, id);
            resident1.setName(resident.getName());
            resident1.set_owner(resident.is_owner());
            resident1.setAge(resident.getAge());
            resident1.setUses_elevator(resident.isUses_elevator());
            resident1.setContract_start(resident.getContract_start());
            resident1.setHas_paid_monthly_fee(resident.isHas_paid_monthly_fee());
            resident1.setLast_payment(resident.getLast_payment());
            resident1.setPet_uses_common_areas(resident.isPet_uses_common_areas());
            session.persist(resident1);
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
