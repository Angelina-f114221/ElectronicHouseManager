package org.example.service;
// commit 2
import org.example.configuration.SessionFactoryUtil;
import org.example.dao.BillingDao;
import org.example.dao.DaoUtil;
import org.example.entity.Apartment;
import org.hibernate.Session;

public class BillingService {

    public static double calculateMonthlyFeeForApartment(long apartmentId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment1 = DaoUtil.require(session, Apartment.class, apartmentId);

            long residents = BillingDao.countResidentsOver7UsingElevator(session, apartmentId);

            double feeArea = apartment1.getArea() * apartment1.getBuilding().getFee_per_sqm();
            double feePets = apartment1.getPets_using_ca() * apartment1.getBuilding().getFee_per_pet_using_ca();
            double feeElevator = residents * apartment1.getBuilding().getFee_per_person_over_7_using_elevator();

            return feeArea + feePets + feeElevator;
        }
    }
}
