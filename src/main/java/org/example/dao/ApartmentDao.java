package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.ApartmentDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class ApartmentDao {

    public static void createApartment(ApartmentDto apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Building building1 = require(session, Building.class, apartment.getBuilding_id());

                Apartment apartment1 = new Apartment();
                apartment1.setNumber(apartment.getNumber());
                apartment1.setFloor(apartment.getFloor());
                apartment1.setArea(apartment.getArea());
                apartment1.setPets_using_ca(apartment.getPets_using_ca());
                apartment1.setBuilding(building1);

                session.persist(apartment1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static List<ApartmentDto> getApartments() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.ApartmentDto(
                    a.id,
                    a.number,
                    a.floor,
                    a.area,
                    a.pets_using_ca,
                    a.building.id
                )
                FROM Apartment a
            """, ApartmentDto.class).getResultList();
        }
    }

    public static ApartmentDto getApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.ApartmentDto(
                    a.id,
                    a.number,
                    a.floor,
                    a.area,
                    a.pets_using_ca,
                    a.building.id
                )
                FROM Apartment a
                WHERE a.id = :id
            """, ApartmentDto.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Apartment with id=" + id + " not found"));
        }
    }

    public static void updateApartment(long id, ApartmentDto apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Apartment apartment1 = require(session, Apartment.class, id);
                Building building1 = require(session, Building.class, apartment.getBuilding_id());

                apartment1.setNumber(apartment.getNumber());
                apartment1.setFloor(apartment.getFloor());
                apartment1.setArea(apartment.getArea());
                apartment1.setPets_using_ca(apartment.getPets_using_ca());
                apartment1.setBuilding(building1);

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
