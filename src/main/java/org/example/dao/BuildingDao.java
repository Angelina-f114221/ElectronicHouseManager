package org.example.dao;

import jakarta.persistence.EntityNotFoundException;
import org.example.configuration.SessionFactoryUtil;
import org.example.dto.BuildingDto;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.service.ValidationUtil;

import java.util.List;

import static org.example.dao.DaoUtil.require;

public class BuildingDao {

    public static void createBuilding(BuildingDto building) {
        ValidationUtil.validateOrThrow(building);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Company company = null;
                if (building.getCompany_id() != null && building.getCompany_id() > 0) {
                    company = require(session, Company.class, building.getCompany_id());
                }

                Employee employee = require(session, Employee.class, building.getEmployee_id());

                Building building1 = new Building();
                building1.setName(building.getName());
                building1.setFloors(building.getFloors());
                building1.setAddress(building.getAddress());
                building1.setCommon_areas(building.getCommon_areas());
                building1.setTotal_areas(building.getTotal_areas());
                building1.setContract_start_date(building.getContract_start_date());
                building1.setFee_per_sqm(building.getFee_per_sqm());
                building1.setFee_per_pet_using_ca(building.getFee_per_pet_using_ca());
                building1.setFee_per_person_over_7_using_elevator(building.getFee_per_person_over_7_using_elevator());

                building1.setCompany(company);
                building1.setEmployee(employee);

                session.persist(building1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static List<BuildingDto> getBuildings() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.BuildingDto(
                    b.id,
                    b.name,
                    b.floors,
                    b.address,
                    b.common_areas,
                    b.total_areas,
                    b.contract_start_date,
                    b.fee_per_sqm,
                    b.fee_per_pet_using_ca,
                    b.fee_per_person_over_7_using_elevator,
                    c.id,
                    e.id
                )
                FROM Building b
                LEFT JOIN b.company c
                LEFT JOIN b.employee e
            """, BuildingDto.class).getResultList();
        }
    }

    public static BuildingDto getBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                SELECT new org.example.dto.BuildingDto(
                    b.id,
                    b.name,
                    b.floors,
                    b.address,
                    b.common_areas,
                    b.total_areas,
                    b.contract_start_date,
                    b.fee_per_sqm,
                    b.fee_per_pet_using_ca,
                    b.fee_per_person_over_7_using_elevator,
                    c.id,
                    e.id
                )
                FROM Building b
                LEFT JOIN b.company c
                LEFT JOIN b.employee e
                WHERE b.id = :id
            """, BuildingDto.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Building with id=" + id + " not found"));
        }
    }

    public static void updateBuilding(long id, BuildingDto building) {
        ValidationUtil.validateOrThrow(building);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Building building1 = require(session, Building.class, id);

                Company company = null;
                if (building.getCompany_id() != null && building.getCompany_id() > 0) {
                    company = require(session, Company.class, building.getCompany_id());
                }

                Employee employee = require(session, Employee.class, building.getEmployee_id());

                building1.setName(building.getName());
                building1.setFloors(building.getFloors());
                building1.setAddress(building.getAddress());
                building1.setCommon_areas(building.getCommon_areas());
                building1.setTotal_areas(building.getTotal_areas());
                building1.setContract_start_date(building.getContract_start_date());
                building1.setFee_per_sqm(building.getFee_per_sqm());
                building1.setFee_per_pet_using_ca(building.getFee_per_pet_using_ca());
                building1.setFee_per_person_over_7_using_elevator(building.getFee_per_person_over_7_using_elevator());

                building1.setCompany(company);
                building1.setEmployee(employee);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    public static void deleteBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Building building1 = require(session, Building.class, id);
                session.remove(building1);
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
