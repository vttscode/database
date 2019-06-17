package com.codeacademy.hibernatetutorial;

import com.codeacademy.hibernatetutorial.model.Company;
import com.codeacademy.hibernatetutorial.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class H2DBExample {

    public static void main(String[] theory) throws InterruptedException {
        SessionFactory sessionFactory = HibernateUtil
                .getSessionFactory("hibernate.cfg.h2tut.xml");
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        Company company = new Company();
        company.setName("Any name");
        session.save(company);

        session.getTransaction().commit();
        List<Company> companies = session.createQuery("from Company").list();
        System.out.println("Companies: " + companies);

        sessionFactory.close();
    }

}
