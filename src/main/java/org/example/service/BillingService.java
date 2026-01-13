package org.example.service;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.BillingDao;
import org.example.dao.DaoUtil;
import org.example.entity.Apartment;
import org.example.entity.Fee;
import org.hibernate.Session;
import java.math.BigDecimal;

public class BillingService {

    public static BigDecimal calculateMonthlyFeeForApartment(long apartment_id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment = DaoUtil.require(session, Apartment.class, apartment_id);
            long residentsCount = BillingDao.countResidentsOver7UsingElevator(session, apartment_id);

            // Вземаме най-новата такса за сградата
            Fee latestFee = BillingDao.getLatestFeeForBuilding(session, apartment.getBuilding().getId());

            if (latestFee == null) {
                return BigDecimal.ZERO;
            }

            BigDecimal feeArea = apartment.getArea().multiply(latestFee.getFee_per_sqm());
            BigDecimal feePets = BigDecimal.valueOf(apartment.getPets_using_ca()).multiply(latestFee.getFee_per_pet_using_ca());
            BigDecimal feeElevator = BigDecimal.valueOf(residentsCount).multiply(latestFee.getFee_per_person_over_7_using_elevator());

            return feeArea.add(feePets).add(feeElevator);
        }
    }
}