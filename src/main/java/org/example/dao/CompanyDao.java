package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

// искам операциите, които са изпълняване на заявките да бъдат имплементирани в този слой. ще изграя по един DAO клас за всеки ентити модел освен base entity, тъй като той не съдържа специфични данни.
// Това е клас за заявки и не е свързан с инстанционен контекст - данните са строго отделени от заявките.

public class CompanyDao {
    // заявка за създаване на компания. Ще бъде в статичен контекст, методът има нужда от данните за обекта компания. като стратегия се подава модел, от който данните се взимат. Ще използвам обекта session, за да отворя сесията и конекцията към базата. За да създам компанията се използва обекта трансакция, който се вижда от Object Relational Mapping технологията. това е изпълнение на заявки - всички общо или нито една – със заключително действие. То може да е commit и да променя състоянието на базата или да е ролбек - ако някакво изключение се хвърли. ще използвам трансакцията, за да запиша подадения като аргумент обект company през отворената сесия. И накрая имам commit на трансакцията,
    public static void createCompany(Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }
    }
}
