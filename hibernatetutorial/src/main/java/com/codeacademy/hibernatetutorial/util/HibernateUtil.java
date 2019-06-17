package com.codeacademy.hibernatetutorial.util;

import com.codeacademy.hibernatetutorial.model.*;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    private static Logger logger = Logger.getLogger(HibernateUtil.class);

    public static SessionFactory buildSessionFactory(String config) {
        try {
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(Company.class)
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(Contact.class)
                    .addAnnotatedClass(Address.class)
                    .addAnnotatedClass(Person.class)
                    .addAnnotatedClass(Salary.class)
                    .addAnnotatedClass(Expense.class)
                    .addAnnotatedClass(Branch.class);
            configuration.configure(config == null ? "hibernate.cfg.xml" : config);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactoryWithCacheConfig() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();

                Map<String, Object> settings = new HashMap<>();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/playground?serverTimezone=UTC&useSSL=false");
                settings.put(Environment.USER, "medardas");
                settings.put(Environment.PASS, "belekas");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.USE_QUERY_CACHE, "true");
                settings.put("hibernate.javax.cache.uri", "ehcache.xml");

                // Enable second level cache (default value is true)
                settings.put(Environment.USE_SECOND_LEVEL_CACHE, true);

                // Specify cache region factory class
                settings.put(Environment.CACHE_REGION_FACTORY,
                        "org.hibernate.cache.jcache.JCacheRegionFactory");

                // Specify cache provider
                settings.put("hibernate.javax.cache.provider",
                        "org.ehcache.jsr107.EhcacheCachingProvider");

                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();
                MetadataSources sources = new MetadataSources(registry)
                        .addAnnotatedClass(Expense.class);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(String config) {
        if (sessionFactory == null) sessionFactory = buildSessionFactory(config);
        return sessionFactory;
    }
}
