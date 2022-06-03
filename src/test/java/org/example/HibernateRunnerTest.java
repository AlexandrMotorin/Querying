package org.example;

import org.example.querying.model.Department;
import org.example.util.HibernateTestUtil;
import org.junit.jupiter.api.Test;

public class HibernateRunnerTest {

    @Test
    void checkTestContainer(){
        try(var sessionFactory = HibernateTestUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Department department = Department.builder()
                    .name("HR")
                    .build();

            session.persist(department);

            session.getTransaction().commit();
        }

    }
}
