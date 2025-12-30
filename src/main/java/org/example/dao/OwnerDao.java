package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class OwnerDao {
    public static void createOwner(Owner owner) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(owner);
            transaction.commit();
        }
    }
    public static List<Owner> getOwners() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT o FROM Owner o", Owner.class)
                    .getResultList();
        }
    }
    public static Owner getOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Owner.class, id);
        }
    }
    public static void updateOwner(long id, Owner owner) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Owner owner1 = require(session, Owner.class, id);
                owner1.setName(owner.getName());
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
    public static void deleteOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Owner owner1 = require(session, Owner.class, id);
                session.remove(owner1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
