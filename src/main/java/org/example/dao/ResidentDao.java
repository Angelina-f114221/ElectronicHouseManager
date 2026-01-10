package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.ResidentDto;
import org.example.entity.Apartment;
import org.example.entity.Resident;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class ResidentDao {

    public static void createResident(ResidentDto resident) {
        ValidationUtil.validateOrThrow(resident);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Apartment apartment1 = require(session, Apartment.class, resident.getApartment_id());

                Resident resident1 = new Resident();
                resident1.setName(resident.getName());
                resident1.setAge(resident.getAge());
                resident1.setUses_elevator(resident.isUses_elevator());
                resident1.setContract_start(resident.getContract_start());
                resident1.setApartment(apartment1);

                session.persist(resident1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static List<ResidentDto> getResidents() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.ResidentDto(
                    r.id,
                    r.name,
                    r.age,
                    r.uses_elevator,
                    r.contract_start,
                    r.apartment.id
                )
                FROM Resident r
            """, ResidentDto.class).getResultList();
        }
    }

    public static ResidentDto getResident(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.ResidentDto(
                    r.id,
                    r.name,
                    r.age,
                    r.uses_elevator,
                    r.contract_start,
                    r.apartment.id
                )
                FROM Resident r
                WHERE r.id = :id
            """, ResidentDto.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Resident with id=" + id + " not found"));
        }
    }

    public static void updateResident(long id, ResidentDto resident) {
        ValidationUtil.validateOrThrow(resident);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Resident resident1 = require(session, Resident.class, id);
                Apartment apartment1 = require(session, Apartment.class, resident.getApartment_id());

                resident1.setName(resident.getName());
                resident1.setAge(resident.getAge());
                resident1.setUses_elevator(resident.isUses_elevator());
                resident1.setContract_start(resident.getContract_start());
                resident1.setApartment(apartment1);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static void deleteResident(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Resident resident1 = require(session, Resident.class, id);
                session.remove(resident1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
