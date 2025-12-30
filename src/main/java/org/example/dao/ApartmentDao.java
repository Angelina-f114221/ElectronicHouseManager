package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static org.example.dao.DaoUtil.require;

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
            try {
                Apartment apartment1 = require(session, Apartment.class, id);

                apartment1.setArea(apartment.getArea());
                apartment1.setFloor(apartment.getFloor());
                apartment1.setNumber(apartment.getNumber());
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
    public static void deleteApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Apartment apartment1 = require(session, Apartment.class, id);
                session.remove(apartment1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
