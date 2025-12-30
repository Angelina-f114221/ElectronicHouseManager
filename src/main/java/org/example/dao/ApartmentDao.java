package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentDao {
    public static void createApartment(Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(apartment);
            transaction.commit();
        }
    }
    public static List<Apartment> getApartments() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT a FROM Apartment a", Apartment.class)
                    .getResultList();
        }
    }
    public static Apartment getApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Apartment.class, id);
        }
    }
    public static void updateApartment(long id, Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment1 = session.find(Apartment.class, id);
            if (apartment1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Apartment with id=" + id + " not found");
            }
            apartment1.setArea(apartment.getArea());
            apartment1.setFloor(apartment.getFloor());
            apartment1.setNumber(apartment.getNumber());
            transaction.commit();
        }
    }
    public static void deleteApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment1 = session.find(Apartment.class, id);
            session.remove(apartment1);
            transaction.commit();
        }
    }
}
