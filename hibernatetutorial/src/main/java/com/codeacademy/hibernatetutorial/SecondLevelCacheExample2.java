package com.codeacademy.hibernatetutorial;

import com.codeacademy.hibernatetutorial.model.Employee;
import com.codeacademy.hibernatetutorial.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.stat.Statistics;

public class SecondLevelCacheExample2 {

    private static Logger logger = Logger
            .getLogger(SecondLevelCacheExample2.class);

    public static void main(String[] args) {
        DOMConfigurator.configure("log4j.xml");

        logger.log(AppLogger.APPLOGGER, "Temp Dir: " + System.getProperty("java.io.tmpdir"));


        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(null);
        //EntityCreator.deleteAllEmployeesFromId(sessionFactory.getCurrentSession(), 1);
        //setUpDatabase(sessionFactory.openSession());

        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        Session session = sessionFactory.openSession();
        Session otherSession = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Transaction otherTransaction = otherSession.beginTransaction();

        printStats(stats, 0);

        Employee emp = session.load(Employee.class, 10002);
        printData(emp, stats, 1);

        emp = session.load(Employee.class, 10002);
        printData(emp, stats, 2);

        session.evict(emp);
        emp = session.load(Employee.class, 10002);
        printData(emp, stats, 3);

        emp = session.load(Employee.class, 10003);
        printData(emp, stats, 4);

        emp = otherSession.load(Employee.class, 10002);
        printData(emp, stats, 5);

        transaction.commit();
        otherTransaction.commit();
        sessionFactory.close();
    }

    private static void printStats(Statistics stats, int i) {
        CacheRegionStatistics secondLevelCacheStatistics =
                stats.getDomainDataRegionStatistics("employee");
        logger.log(AppLogger.APPLOGGER, "***** " + i + " *****");
        logger.log(AppLogger.APPLOGGER, "Fetch Count="
                + stats.getEntityFetchCount());
        logger.log(AppLogger.APPLOGGER, "Second Level Hit Count="
                + secondLevelCacheStatistics.getHitCount());
        logger.log(AppLogger.APPLOGGER, "Second Level Miss Count="
                + secondLevelCacheStatistics.getMissCount());
        logger.log(AppLogger.APPLOGGER, "Second Level Put Count="
                + secondLevelCacheStatistics.getPutCount());
    }

    private static void printData(Employee emp, Statistics stats, int count) {
        logger.log(AppLogger.APPLOGGER, count + ":: Name = " + emp.getName() + ", City = " + emp.getAddress().getCity());
        printStats(stats, count);
    }
/*
    private static void setUpDatabase(Session session) {
        clearAddresses(session, 0);
        clearEmployees(session, 0);
        session.getTransaction().begin();
        session.save(createEmployeeInCompany());
        session.save(createEmployeeInCompany());
        session.save(createEmployeeInCompany());
        session.getTransaction().commit();
        session.close();
    }*/
}
