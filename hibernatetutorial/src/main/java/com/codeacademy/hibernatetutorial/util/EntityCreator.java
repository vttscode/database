package com.codeacademy.hibernatetutorial.util;

import com.codeacademy.hibernatetutorial.AppLogger;
import com.codeacademy.hibernatetutorial.model.Address;
import com.codeacademy.hibernatetutorial.model.Company;
import com.codeacademy.hibernatetutorial.model.Employee;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Random;

public class EntityCreator {
    private static final String COMPANY_1 = "PROGRAMUOK 1";
    private static final String BRANCH_1 = "Sunrise valley 1";
    private static final String BRANCH_2 = "Tech Park 1";

    private static Logger logger = Logger.getLogger(EntityCreator.class);

    public static Employee createEmployeeInCompany(Session session,String companyName) {
        Company company = EntityFinder.findCompanyByName(session, companyName);
        Employee employee = new Employee();
        employee.setCompany(company);
        Address address = new Address();
        address.setStreet(random15CharString());
        address.setCity("Vilnius");
        employee.setAddress(address);
        employee.setPosition("Developer");
        employee.setName(random15CharString());
        return employee;
    }

    public static void clearEmployees(Session session, int deleteFromIds) {
        session.getTransaction().begin();
        CriteriaDelete<Employee> criteriaDelete = deleteFromId(session, Employee.class, deleteFromIds);
        session.createQuery(criteriaDelete).executeUpdate();
        session.getTransaction().commit();
    }

    public static void clearAddresses(Session session, int deleteFromIds) {
        session.getTransaction().begin();
        CriteriaDelete<Address> criteriaDelete = EntityCreator.deleteFromId(session, Address.class, deleteFromIds);
        session.createQuery(criteriaDelete).executeUpdate();
        session.getTransaction().commit();
    }

    public static Company createCompany(String companyName,Session session) {
        Address companyAddress = new Address();
        companyAddress.setCity("Vilnius");
        companyAddress.setStreet(random15CharString());

        Company company = new Company();
        company.setName(companyName);
        company.setAddress(companyAddress);
        save(session, company);

        return company;
    }

    public static Company findByName(Session session, String companyName) {
        Query companyByName = session.createQuery("from Company c where c.name=:companyName");
        companyByName.setParameter("companyName", companyName);
        try {
            return (Company) companyByName.list().stream().findFirst().get();
        } catch (RuntimeException re) {
            return null;
        }
    }

    public static Company getCompany(Session session,String companyName) {
        Company company = findByName(session, companyName);
        if (company == null) {
            company = createCompany(companyName,session);
            save(session, company);
        }
        return company;
    }

    private static void save(Session session, Object o) {
        session.getTransaction().begin();
        session.save(o);
        session.getTransaction().commit();
    }

    public static String random15CharString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
    public static String random2CharString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 2;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static void deleteAllEmployeesFromId(Session session, int idToDeleteFrom) {
        session.getTransaction().begin();
        Query<Employee> query = session
                .createQuery(
                        "SELECT e FROM Employee e " +
                                "WHERE e.id >= :idToDeleteFrom");
        query.setParameter("idToDeleteFrom", idToDeleteFrom);
        List<Employee> employeeIds = query.list();
        for (int i = 0; i < employeeIds.size(); i++) {
            session.delete(employeeIds.get(i));
        }
        logger.log(AppLogger.APPLOGGER, " Employees deleted:" + employeeIds);
        session.getTransaction().commit();
    }

    private static <T> CriteriaDelete<T> deleteFromId(Session session,
                                                      Class entityToDeleteClass,
                                                      int deleteFromIds) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete =
                criteriaBuilder.createCriteriaDelete(entityToDeleteClass);
        Root<T> root = criteriaDelete.from(entityToDeleteClass);
        criteriaDelete.where(criteriaBuilder
                .greaterThanOrEqualTo(root.get("id"), deleteFromIds));
        return criteriaDelete;
    }
}













