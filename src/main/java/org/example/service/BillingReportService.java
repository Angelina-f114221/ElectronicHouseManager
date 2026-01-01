package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.hibernate.Session;

public class BillingReportService {

    public static double getDueSumForBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartmentIds = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.id = :buildingId
            """, Long.class).setParameter("buildingId", buildingId).getResultList();

            double total = 0.0;
            for (Long id : apartmentIds) {
                total += BillingService.calculateMonthlyFeeForApartment(id);
            }
            return total;
        }
    }

    public static double getDueSumForCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartmentIds = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.company.id = :companyId
            """, Long.class).setParameter("companyId", companyId).getResultList();

            double total = 0.0;
            for (Long id : apartmentIds) {
                total += BillingService.calculateMonthlyFeeForApartment(id);
            }
            return total;
        }
    }

    public static double getDueSumForEmployee(long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartmentIds = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.employee.id = :employeeId
            """, Long.class).setParameter("employeeId", employeeId).getResultList();

            double total = 0.0;
            for (Long id : apartmentIds) {
                total += BillingService.calculateMonthlyFeeForApartment(id);
            }
            return total;
        }
    }
}
