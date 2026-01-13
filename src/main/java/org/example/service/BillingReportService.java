package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.hibernate.Session;
import java.math.BigDecimal;

public class BillingReportService {
// изчислява се цялата сума от такси, която трябва да се заплати от живущите в даден асграда. сумират се таксите за всеки апартамент в сградата
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
// изчислява се сумата от такси, която следва да се плати на дадена компания. сумират се таксите за всеки апартамент в сграда към дадената компания
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
    // изчислява се сумата от такси, която следва да се плати на даден служител. сумират се таксите за всеки апартамент в сграда към даден служител
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
