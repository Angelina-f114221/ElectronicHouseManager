package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.DaoUtil;
import org.example.dto.PayRequestDto;
import org.example.entity.Apartment;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class PaymentService {

    public static void pay(PayRequestDto req, String filePath) {
        ValidationUtil.validateOrThrow(req);

        String companyName;
        String employeeName;
        String buildingName;
        long apartmentId;
        BigDecimal amount;
        LocalDate paymentDate;

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
                paymentDate = req.getPayment_date();
                String period = YearMonth.from(paymentDate).toString();

                Payment payment = new Payment();
                payment.setApartment(apartment);
                payment.setPayment_date(paymentDate);
                payment.setAmount(amount);
                payment.setPeriod(period);

                session.persist(payment);
                tx.commit();

                companyName = company.getName();
                employeeName = employee.getName();
                buildingName = building.getName();
                apartmentId = apartment.getId();

            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        }

        PaymentExportService.appendPaymentLine(
                filePath,
                companyName,
                employeeName,
                buildingName,
                apartmentId,
                amount,
                paymentDate
        );
    }
}
