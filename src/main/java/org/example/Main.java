package org.example;
import org.example.dao.CompanyDao;
import org.example.entity.Company;
import org.hibernate.Session;
import org.example.configuration.SessionFactoryUtil;


/* !!!!!!!!!!!!! - week 6, 35:00
Enum options are
payment status: PENDING, PAID, OVERDUE, CANCELLED
contract status: ACTIVE, TERMINATED, EXPIRED
type of person: OWNER, TENANT, GUEST
type of fee: BASE_AREA_FEE, PER_PERSON_FEE, PET_FEE
!!!!!!!!!!!!!!!!
implement dto models
 */

public class Main {
    static void main() {
        // отварям сесията чрез session factory util класа. правя локална променлива и я използвам, за да се конектна към базата и след това да изпълнявам заявки към нея.
        Company company = new Company();
        company.setName("Apple");
        CompanyDao.createCompany(company);
    }
}
