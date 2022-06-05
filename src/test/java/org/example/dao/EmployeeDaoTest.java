package org.example.dao;

import jakarta.persistence.Tuple;
import lombok.Cleanup;
import org.example.querying.dao.EmployeeDao;
import org.example.querying.dto.DepartmentDto;
import org.example.querying.model.Employee;
import org.example.querying.model.Payment;
import org.example.util.HibernateTestUtil;
import org.example.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.text.DecimalFormat;
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
        assertThat(results).hasSize(6);

        session.getTransaction().commit();
    }

    @Test
    void findAllByFirstname(){
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        String employeeName = "Anna";
        List<Employee> results = employeeDao.findAllByFirstName(session,employeeName);
        var resultEmployeeName = results.get(0).getEmployeeInfo().getFirstName();

        assertThat(results).hasSize(1);
        assertThat(resultEmployeeName).isEqualTo(employeeName);

        session.getTransaction().commit();
    }

    @Test
    void findLimitedEmployeesOrderedByBirthday(){
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        int limit = 3;

        List<Employee> results
                = employeeDao.findLimitedEmployeesOrderedByBirthday(session, limit);
        assertThat(results).hasSize(3);

        var names = results.stream().map(e -> e.getEmployeeInfo().getFirstName()).toList();
        assertThat(names).contains("Viktor","Liza","Alexandr");

        session.getTransaction().commit();
    }

    @Test
    void findAllByDepartmentName(){
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        String departmentName = "HR";
        List<Employee> result = employeeDao.findAllByDepartmentName(session, departmentName);

        assertThat(result).hasSize(2);

        var names = result.stream().map(e -> e.getEmployeeInfo().getFirstName()).toList();
        assertThat(names).contains("Anna", "Liza");

        session.getTransaction().commit();
    }

    @Test
    void findAllPaymentsByDepartmentName(){
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        String departmentName = "HR";

        List<Payment> result = employeeDao.findAllPaymentsByDepartmentName(session, departmentName);

        assertThat(result).hasSize(6);

        session.getTransaction().commit();
    }

    @Test
    void findAveragePaymentAmountByFirstAndLastNames(){
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        String firstName = "Anna";
        String lastName = "Chirkova";

        Double result = employeeDao.findAveragePaymentAmountByFirstAndLastNames(session, firstName, lastName);

        assertThat(result).isEqualTo(151.0);

        session.getTransaction().commit();
    }

    @Test
    void findDepartmentNamesWithAvgUserPaymentsOrderedByDepartmentName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<DepartmentDto> results =
                employeeDao.findDepartmentNamesWithAvgUserPaymentsOrderedByDepartmentName(session);

        var depNames = results.stream().map(DepartmentDto::getName).toList();
        assertThat(depNames).contains("Develop", "Design", "HR");

        var avgAmounts = results.stream().map(a ->{
                    var decimalFormat = new DecimalFormat("#0.0");
                    var r = (Double) a.getAmount();
                    return decimalFormat.format(r);
                }).toList();
        assertThat(avgAmounts).contains("149,2","150,3","153,0");

        session.getTransaction().commit();
    }

    @Test
    void isItPossible() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Tuple> results = employeeDao.isItPossible(session);
        assertThat(results).hasSize(4);

        session.getTransaction().commit();
    }
}
