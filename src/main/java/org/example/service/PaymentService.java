package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.DaoUtil;
import org.example.dao.PaymentStatusDao;
import org.example.dto.PayRequestDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Payment;
import org.example.entity.PaymentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class PaymentService {

    public static void pay(PayRequestDto req, String filePath) {
        ValidationUtil.validateOrThrow(req);

        String company_name;
        String employee_name;
        String building_name;
        long apartment_id;
        BigDecimal amount;
        LocalDate payment_date;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Apartment apartment = DaoUtil.require(session, Apartment.class, req.getApartment_id());
                Building building = apartment.getBuilding();

                Company company = building.getCompany();
                Employee employee = building.getEmployee();
                if (company == null || employee == null) {
                    throw new IllegalStateException("Building must have company and employee before payment.");
                }

                amount = BillingService.calculateMonthlyFeeForApartment(apartment.getId());
                payment_date = req.getPayment_date();
                String period = YearMonth.from(payment_date).toString();

                PaymentStatus paidStatus = PaymentStatusDao.getByCode("PAID");
                if (paidStatus == null) {
                    throw new IllegalStateException("PaymentStatus 'PAID' not found. Initialize payment_statuses table.");
                }

                Payment payment = new Payment();
                payment.setApartment(apartment);
                payment.setPayment_date(payment_date);
                payment.setAmount(amount);
                payment.setPeriod(period);
                payment.setStatus(paidStatus);

                session.persist(payment);
                tx.commit();

                company_name = company.getName();
                employee_name = employee.getName();
                building_name = building.getName();
                apartment_id = apartment.getId();

            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }

        PaymentExportService.appendPaymentLine(
                filePath,
                company_name,
                employee_name,
                building_name,
                apartment_id,
                amount,
                payment_date
        );
    }
}
