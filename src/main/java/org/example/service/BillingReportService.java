package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;

public class BillingReportService {

    public static BigDecimal getDueSumForBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartmentIds = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.id = :buildingId
            """, Long.class).setParameter("buildingId", buildingId).getResultList();

            BigDecimal total = BigDecimal.ZERO;
            for (Long id : apartmentIds) {
                total = total.add(BillingService.calculateMonthlyFeeForApartment(id));
            }
            return total;
        }
    }

    public static BigDecimal getDueSumForCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartmentIds = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.company.id = :companyId
            """, Long.class).setParameter("companyId", companyId).getResultList();

            BigDecimal total = BigDecimal.ZERO;
            for (Long id : apartmentIds) {
                total = total.add(BillingService.calculateMonthlyFeeForApartment(id));
            }
            return total;
        }
    }

    public static BigDecimal getDueSumForEmployee(long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartmentIds = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.employee.id = :employeeId
            """, Long.class).setParameter("employeeId", employeeId).getResultList();

            BigDecimal total = BigDecimal.ZERO;
            for (Long id : apartmentIds) {
                total = total.add(BillingService.calculateMonthlyFeeForApartment(id));
            }
            return total;
        }
    }
}
