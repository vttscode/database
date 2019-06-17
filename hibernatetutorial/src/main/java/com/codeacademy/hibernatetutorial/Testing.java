package com.codeacademy.hibernatetutorial;

import com.codeacademy.hibernatetutorial.model.Contact;
import com.codeacademy.hibernatetutorial.model.Employee;
import com.codeacademy.hibernatetutorial.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class Testing {

    public static void main(String[] thoery) {
        printcontactsall();
    }

    private static void printcontactsall() {
        Session session = HibernateUtil.buildSessionFactory(null).getCurrentSession();
        session.getTransaction().begin();


        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = cb.createQuery(Object[].class);
        Root<Contact> contactRoot = criteriaQuery.from(Contact.class);

        //  Root<Contact> contactRoot = criteriaQuery.from( Contact.class );
        Join<Contact, Employee> join1 = contactRoot.join("employee", JoinType.INNER);
        //  Fetch<Contact, Employee> join1 = contactRoot.fetch( "employee");
        //  Join<Contact, Employee> join1 = (Join<Contact, Employee>) fetch;

        criteriaQuery.multiselect(join1.get("id"), join1.get("name"), cb.count(contactRoot.get("value")));
        criteriaQuery.where(cb.equal(contactRoot.get("employee"), join1.get("id")));
        criteriaQuery.groupBy(join1.get("id"));
        criteriaQuery.having(cb.equal(cb.count(contactRoot.get("value")), null));
        TypedQuery<Object[]> query = session.createQuery(criteriaQuery);
        List<Object[]> list = query.getResultList();
        for (Object[] objects : list) {
            String darb = (String) objects[1];
            Integer darbid = (Integer) objects[0];
            Long kiek = (Long) objects[2];

            System.out.println("Darbuotojas = " + darb + "\t id  " + darbid + "\t turitelefonu=" + kiek);
        }
        session.getTransaction().commit();
    }
}
