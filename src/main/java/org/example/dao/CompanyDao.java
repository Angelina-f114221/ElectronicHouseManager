package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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

    /*
    искам да видим компаниите, като направим заявка с read операцията. правя статичен метод, който връща всичките компании като колекция. отварям сесията по същия начин както в createCompany. като резултат, ще получа лист от компании. използвам абстракцията на Company Entity модела, за да селектирам всичките записи. createQuery метода връща заявка. тоест ще бъде изпълнена заявка, която ще получа като резултат през допълнителен метод за резултатната колекция. създавам заявката под формата на символен низ, посочвам резултатния тип (обекти от тип company) - какви са обектите, които ще получа, и след това ще взема резултата. имам overloaded версии на Create query метода. ще спазя конвенцията и ще селектирам от таблицата компания (c e alias за company). Ще използвам Company entity модела и ще го обознача с това, което използвах в стандартната заявка. това дава еквивалент на заявка, написана на native sequel, но описана в рамките на обектноориентирания контекст през entity моделите и с езика JPQL. в заявката се използва името на entity модела, защото това е company Entity модел. когато бъде изпълнена заявката, взимаме резултатната колекция.
     */
    public static List<Company> getCompanies() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM Company c", Company.class)
                    .getResultList();
        }
    }

}
