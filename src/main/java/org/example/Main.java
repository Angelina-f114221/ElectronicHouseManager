package org.example;
import org.hibernate.Session;
import org.example.configuration.SessionFactoryUtil;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        // отварям сесията чрез session factory util класа. правя локална променлива и я използвам, за да се конектна към базата и след това да изпълнявам заявки към нея.
        Session session =  SessionFactoryUtil.getSessionFactory().openSession();
    }
}
