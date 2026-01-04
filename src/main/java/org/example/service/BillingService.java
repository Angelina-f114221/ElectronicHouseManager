package org.example.service;
// commit 2
import org.example.configuration.SessionFactoryUtil;
import org.example.dao.BillingDao;
import org.example.dao.DaoUtil;
import org.example.entity.Apartment;
import org.hibernate.Session;

import java.math.BigDecimal;

public class BillingService {

    public static BigDecimal calculateMonthlyFeeForApartment(long apartmentId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment1 = DaoUtil.require(session, Apartment.class, apartmentId);
            long residentsCount = BillingDao.countResidentsOver7UsingElevator(session, apartmentId);

            BigDecimal feeArea = apartment1.getArea().multiply(apartment1.getBuilding().getFee_per_sqm());
            BigDecimal feePets = BigDecimal.valueOf(apartment1.getPets_using_ca()).multiply(apartment1.getBuilding().getFee_per_pet_using_ca());
            BigDecimal feeElevator = BigDecimal.valueOf(residentsCount).multiply(apartment1.getBuilding().getFee_per_person_over_7_using_elevator());

            return feeArea.add(feePets).add(feeElevator);
        }
    }
}
