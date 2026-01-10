package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.OwnerDto;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class OwnerDao {

    public static void createOwner(OwnerDto owner) {
        ValidationUtil.validateOrThrow(owner);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Owner owner1 = new Owner();
                owner1.setName(owner.getName());
                owner1.setBirth_date(owner.getBirth_date());

                session.persist(owner1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static List<OwnerDto> getOwners() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.OwnerDto(
                    o.id,
                    o.name,
                    o.birth_date
                )
                FROM Owner o
            """, OwnerDto.class).getResultList();
        }
    }

    public static OwnerDto getOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.OwnerDto(
                    o.id,
                    o.name,
                    o.birth_date
                )
                FROM Owner o
                WHERE o.id = :id
            """, OwnerDto.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Owner with id=" + id + " not found"));
        }
    }

    public static void updateOwner(long id, OwnerDto owner) {
        ValidationUtil.validateOrThrow(owner);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Owner owner1 = require(session, Owner.class, id);

                owner1.setName(owner.getName());
                owner1.setBirth_date(owner.getBirth_date());

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
