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

            double fee_area = apartment1.getArea() * apartment1.getBuilding().getFee_per_sqm();
            double fee_pets = apartment1.getPets_using_ca() * apartment1.getBuilding().getFee_per_pet_using_ca();
            double fee_elevator = residents * apartment1.getBuilding().getFee_per_person_over_7_using_elevator();

            return fee_area + fee_pets + fee_elevator;
        }
    }
}
