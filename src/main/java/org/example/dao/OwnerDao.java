package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.OwnerDto;
import org.example.entity.Apartment;
import org.example.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.example.dao.DaoUtil.require;

public class OwnerDao {

    public static void createOwner(OwnerDto owner) {
        ValidationUtil.validateOrThrow(owner);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Set<Apartment> apartments = new HashSet<>();
                if (owner.getApartment_ids() != null && !owner.getApartment_ids().isEmpty()) {
                    for (Long apartment_id : owner.getApartment_ids()) {
                        Apartment apartment = require(session, Apartment.class, apartment_id);
                        apartments.add(apartment);
                    }
                }

                Owner owner1 = new Owner();
                owner1.setName(owner.getName());
                owner1.setBirth_date(owner.getBirth_date());
                owner1.setApartments(apartments);

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
            List<Owner> owners = session.createQuery("""
                FROM Owner o
                LEFT JOIN FETCH o.apartments
            """, Owner.class).getResultList();

            return owners.stream()
                    .map(o -> new OwnerDto(
                            o.getId(),
                            o.getName(),
                            o.getBirth_date(),
                            o.getApartments().stream().map(Apartment::getId).collect(java.util.stream.Collectors.toSet())
                    ))
                    .toList();
        }
    }

    public static OwnerDto getOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Owner owner = session.createQuery("""
                FROM Owner o
                LEFT JOIN FETCH o.apartments
                WHERE o.id = :id
            """, Owner.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Owner with id=" + id + " not found"));

            return new OwnerDto(
                    owner.getId(),
                    owner.getName(),
                    owner.getBirth_date(),
                    owner.getApartments().stream().map(Apartment::getId).collect(java.util.stream.Collectors.toSet())
            );
        }
    }

    public static void updateOwner(long id, OwnerDto owner) {
        ValidationUtil.validateOrThrow(owner);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Owner owner1 = require(session, Owner.class, id);

                Set<Apartment> apartments = new HashSet<>();
                if (owner.getApartment_ids() != null && !owner.getApartment_ids().isEmpty()) {
                    for (Long apartment_id : owner.getApartment_ids()) {
                        Apartment apartment = require(session, Apartment.class, apartment_id);
                        apartments.add(apartment);
                    }
                }

                owner1.setName(owner.getName());
                owner1.setBirth_date(owner.getBirth_date());
                owner1.setApartments(apartments);

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
