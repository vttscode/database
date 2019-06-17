package com.codeacademy.hibernatetutorial;

import com.codeacademy.hibernatetutorial.model.Expense;
import com.codeacademy.hibernatetutorial.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.stat.Statistics;

import static com.codeacademy.hibernatetutorial.AppLogger.APPLOGGER;

public class SecondLevelCacheExample {
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactoryWithCacheConfig();
    private static final Session session = sessionFactory.openSession();
    private static final Session session2 = sessionFactory.openSession();

    private static Logger logger = Logger
            .getLogger(SecondLevelCacheExample.class);

    public static void main(String[] args) throws InterruptedException {
        DOMConfigurator.configure("log4j.xml");
        logger.log(APPLOGGER, "Temp Dir: " + System.getProperty("java.io.tmpdir"));
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        session.getTransaction().begin();
        session2.getTransaction().begin();

        long startTime = System.currentTimeMillis();
        Expense expense = session.find(Expense.class, 1l);
        logger.log(APPLOGGER, "New object with id " + expense.getId() + " query took: " + (System.currentTimeMillis() - startTime));
        printStats(stats, 1);
        session.evict(expense);

        long startTime2 = System.currentTimeMillis();
        Expense expense2 = session.load(Expense.class, 1l);
        logger.log(APPLOGGER, "Second query for same object id " + expense2.getId() + " right after first query took: " + (System.currentTimeMillis() - startTime2));
        printStats(stats, 2);
        session.evict(expense2);

        long startTime3 = System.currentTimeMillis();
        Expense expense3 = session2.load(Expense.class, 2l);
        logger.log(APPLOGGER, "New object with id " + expense3.getId() + " query took: " + (System.currentTimeMillis() - startTime3));
        printStats(stats, 3);
        session.evict(expense3);

        //Thread.sleep(6000);

        long startTime4 = System.currentTimeMillis();
        Expense expense4 = session2.load(Expense.class, 2l);
        logger.log(APPLOGGER, "Second query for same object id " + expense4.getId() + " after 5 seconds took: " + (System.currentTimeMillis() - startTime4));
        printStats(stats, 4);

        session.getTransaction().commit();
        session2.getTransaction().commit();
        sessionFactory.close();
    }

    private static void printStats(Statistics stats, int i) {
        CacheRegionStatistics secondLevelCacheStatistics =
                stats.getDomainDataRegionStatistics("expense");
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
}
