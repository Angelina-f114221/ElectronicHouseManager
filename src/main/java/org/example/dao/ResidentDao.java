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
        // проверява дали всичко е наред с анотациите
        ValidationUtil.validateOrThrow(resident);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                // проверка дали апартамента съществува
                Apartment apartment1 = require(session, Apartment.class, resident.getApartment_id());

                Resident resident1 = new Resident();
                resident1.setName(resident.getName());
                resident1.setBirth_date(resident.getBirth_date());
                resident1.setUses_elevator(resident.isUses_elevator());
                resident1.setContract_start(resident.getContract_start());
                resident1.setApartment(apartment1);
                // маркирам за инсърт, подготвям и все още не пращам
                session.persist(resident1);
                // изпраща всички операции в базата данни
                transaction.commit();
            } catch (RuntimeException exception) {
                // връща базата към старото състояние
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
                    r.birth_date,
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
                    r.birth_date,
                    r.uses_elevator,
                    r.contract_start,
                    r.apartment.id
                )
                FROM Resident r
                WHERE r.id = :id
            """, ResidentDto.class)
                    // предпазва от sql injection, понеже е вмъкната стойност
                    .setParameter("id", id)
                    // резултатите като поток, множество редове
                    .getResultStream()
                    // взема само първия елемент, защото ключовете са уникални
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
                resident1.setBirth_date(resident.getBirth_date());
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