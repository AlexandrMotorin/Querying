package org.example;

import lombok.Cleanup;
import org.example.querying.dao.EmployeeDao;
import org.example.querying.model.Employee;
import org.example.util.HibernateTestUtil;
import org.example.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class EmployeeDaoTest {

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final EmployeeDao employeeDao = EmployeeDao.INSTANCE;

    @BeforeAll
    public void initDb(){
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    public void finish(){
        sessionFactory.close();
    }

    @Test
    void findAll(){
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Employee> results = employeeDao.findAll(session);
        assertThat(results).hasSize(5);

        session.getTransaction().commit();
    }

}
