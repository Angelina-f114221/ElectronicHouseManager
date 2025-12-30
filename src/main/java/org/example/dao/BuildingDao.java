package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Building;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingDao {
    public static void createBuilding(Building building) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(building);
            transaction.commit();
        }
    }
    public static List<Building> getBuildings() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT b FROM Building b", Building.class)
                    .getResultList();
        }
    }
    public static Building getBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Building.class, id);
        }
    }
    public static void updateBuilding(long id, Building building) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Building building1 = session.find(Building.class, id);
            if  (building1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Building with id=" + id + " not found");
            }
            building1.setName(building.getName());
            building1.setAddress(building.getAddress());
            building1.setCommon_areas(building.getCommon_areas());
            building1.setFee_per_pet_using_ca(building.getFee_per_pet_using_ca());
            building1.setFee_per_sqm(building.getFee_per_sqm());
            building1.setTotal_areas(building.getTotal_areas());
            transaction.commit();
        }
    }
    public static void deleteBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Building building1 = session.find(Building.class, id);
            if (building1 == null) {
                transaction.rollback();
                throw new EntityNotFoundException("Building with id=" + id + " not found");
            }
            session.remove(building1);
            transaction.commit();
        }
    }
}
