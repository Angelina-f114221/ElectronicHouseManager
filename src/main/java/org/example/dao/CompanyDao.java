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

    /*
    Искам да мога да намирам записи по уникалния им идентификатор. Като аргумент ще бъде подадено id-то. през сесията ще използвам метод, който може да намери записа през ентити обекта, който ми трябва. ще заменя извикването на метода createQuery с извикване на метода find, който ще намери записа със съответното ID. ще кажа, че искам да получа company класа и подавам съответното ID на записа, за да намеря компанията, която търся.

     */
    public static Company getCompany(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Company.class, id);
        }
    }

    /*
    По принцип, когато се ъпдейтва, се изпълнява единична операция. бълк ъпдейти и delete са опасни. Методът приема като аргумент id-то (първичния ключ) и обект, който дава свойствата за обновяване. първо трябва да намеря компанията, която ще бъде ъпдейтвана. Създавам обект company, kойто ще е равен на резултата от метода session.find с аргументи за резултатния тип – company, и самото id. Променяме му името с това на обекта-аргумент. Трябва да се извика метода persist върху обекта, който искам да бъде с обновените данни и трансакцията да бъде камитната. в зависимост от това къде започва трансакцията, ще има значение дали ще има rollback (връщане на предходно състояние, ако има проблем при търсенето на обекта). трансакцията трябва да започне още преди да търся обекта. Има през session persist с аргумент company1 през управлението на състоянията на eнтити моделите и трябва да имаме transaction commit, за да бъде изпълнена трансакцията.

     */
    public static void updateCompany(long id, Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company1 = session.find(Company.class, id);
            company1.setName(company.getName());
            session.persist(company1);
            transaction.commit();
        }
    }

    /*
    правя операцията Delete, като намирам обекта по ID-то и след това го изтривам. Като открия обекта с find метода през session, няма да сетвам никакви стойности на нейните полета. вместо да извикам метода persist, ще извикам метода remove.
     */
    public static void deleteCompany(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company1 = session.find(Company.class, id);
            session.remove(company1);
            transaction.commit();
        }
    }

}
