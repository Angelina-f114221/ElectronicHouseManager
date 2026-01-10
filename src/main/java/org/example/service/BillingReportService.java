package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;

public class BillingReportService {

    public static BigDecimal getDueSumForBuilding(long building_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartment_ids = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.id = :building_id
            """, Long.class).setParameter("building_id", building_id).getResultList();

            BigDecimal total = BigDecimal.ZERO;
            for (Long id : apartment_ids) {
                total = total.add(BillingService.calculateMonthlyFeeForApartment(id));
            }
            return total;
        }
    }

    public static BigDecimal getDueSumForCompany(long company_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartment_ids = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.company.id = :company_id
            """, Long.class).setParameter("company_id", company_id).getResultList();

            BigDecimal total = BigDecimal.ZERO;
            for (Long id : apartment_ids) {
                total = total.add(BillingService.calculateMonthlyFeeForApartment(id));
            }
            return total;
        }
    }

    public static BigDecimal getDueSumForEmployee(long employee_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            var apartment_ids = session.createQuery("""
                SELECT a.id
                FROM Apartment a
                WHERE a.building.employee.id = :employee_id
            """, Long.class).setParameter("employee_id", employee_id).getResultList();

            BigDecimal total = BigDecimal.ZERO;
            for (Long id : apartment_ids) {
                total = total.add(BillingService.calculateMonthlyFeeForApartment(id));
            }
            return total;
        }
    }
}
