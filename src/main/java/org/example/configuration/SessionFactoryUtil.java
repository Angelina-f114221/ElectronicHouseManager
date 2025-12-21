package org.example.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/*
конфигурирам как се отваря сесията, за да се изпълни конекция към базата. включва се object relational mapping технологията, която ни служи за връзка между релационния и обектноориентирания модел, за да мога да отворя сесията.
технологичните компоненти, които дават връзка между Java и базата, са по стандарта JDBC. Hibernate технологията като object relation ping надгражда над този междинен слой между релационния и обектноориентирания модел, като добавя абстрактни класове и методи за изпълнение на основни операции за трансакционно поведение, за отваряне на сесии, за стратегии за автоматизирано генериране на първичните ключове. Session Factory класът е от външната зависимост Hibernate. От тази зависимост мога да изпълнявам трансакции, да отварям сесия, да я затварям, да конфигурирам какво ще бъде проследено като клас и респективно таблица в базата. използвам configuration от Session Factory, за да билдна сесията през предварително дефиниран билдър и да се отвори сесията.
тя няма да се променя често, но ще се добавят нови части от имплементацията на get session factory метод, когато искам да правя нови таблици и да свързвам с тях ентити модели. Тоест този configuration обект служи за да регистрирам нови класове, които ще се грижат за таблици в базата. останалото остава непроменено със съответното билдване, взимайки свойствата на тази конфигурация и най-накрая се връща този session factory обект.
*/

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}
