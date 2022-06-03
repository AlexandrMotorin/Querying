package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.querying.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

@UtilityClass
public class HibernateTestUtil {

    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:13");

    static {
        postgres.start();
    }

    public static SessionFactory buildSessionFactory(){
        Configuration configuration = HibernateUtil.buildConfiguration();

        Properties dbProperties = new Properties();
        dbProperties.put("hibernate.connection.url", postgres.getJdbcUrl());
        dbProperties.put("hibernate.connection.username", postgres.getUsername());
        dbProperties.put("hibernate.connection.password", postgres.getPassword());

        configuration.setProperties(dbProperties);
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}
