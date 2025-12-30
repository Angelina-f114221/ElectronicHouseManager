package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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
            Owner owner1 = session.find(Owner.class, id);
            if (owner1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Owner with id=" + id + " not found");
            }
            owner1.setName(owner.getName());
            transaction.commit();
        }
    }
    public static void deleteOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Owner owner1 = session.find(Owner.class, id);
            if (owner1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Owner with id=" + id + " not found");
            }
            session.remove(owner1);
            transaction.commit();
        }
    }
}
