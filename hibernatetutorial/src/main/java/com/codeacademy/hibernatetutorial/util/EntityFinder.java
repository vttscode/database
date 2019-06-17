package com.codeacademy.hibernatetutorial.util;

import com.codeacademy.hibernatetutorial.model.Address;
import com.codeacademy.hibernatetutorial.model.Company;
import com.codeacademy.hibernatetutorial.model.Employee;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class EntityFinder {

    public static Employee findEmployeeByName(Session session, String name) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaFindByName = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaFindByName.from(Employee.class);
        criteriaFindByName.where(criteriaBuilder.equal(root.get("name"), name));
        return session.createQuery(criteriaFindByName).getSingleResult();
    }

    public static Address findAddressByStreet(Session session, String street) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Address> criteriaFindByStreet = criteriaBuilder.createQuery(Address.class);
        Root<Address> root = criteriaFindByStreet.from(Address.class);
        criteriaFindByStreet.where(criteriaBuilder.equal(root.get("street"), street));
        return session.createQuery(criteriaFindByStreet).getSingleResult();
    }
    public static Company findCompanyByName(Session session, String name) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaFindByName = criteriaBuilder.createQuery(Company.class);
        Root<Company> root = criteriaFindByName.from(Company.class);
        criteriaFindByName.where(criteriaBuilder.equal(root.get("name"), name));
        return session.createQuery(criteriaFindByName).getSingleResult();
    }

}
