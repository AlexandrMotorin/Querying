package org.example.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.example.querying.model.Department;
import org.example.querying.model.Employee;
import org.example.querying.model.EmployeeInfo;
import org.example.querying.model.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory){
        @Cleanup var session = sessionFactory.openSession();

        Department hr = saveDepartment(session, "HR");
        Department design = saveDepartment(session, "Design");
        Department develop = saveDepartment(session, "Develop");

        var sasha = saveEmployee(session, "Alexandr", "Motorin",
                LocalDate.of(1996, 12, 10), develop);
        var bogdan = saveEmployee(session, "Bogdan", "Pelevin",
                LocalDate.of(1997, 6, 11), develop);

        var viktor = saveEmployee(session, "Viktor", "Ivanov",
                LocalDate.of(1994, 8, 14), design);
        var elena = saveEmployee(session, "Elena", "Popova",
                LocalDate.of(1997, 11, 28), design);

        var anna = saveEmployee(session, "Anna", "Chirkova",
                LocalDate.of(1998, 10, 8), hr);
        var liza = saveEmployee(session, "Liza", "Tetereva",
                LocalDate.of(1995, 7, 16), hr);

        savePayment(session, 100, sasha);
        savePayment(session, 150, sasha);
        savePayment(session, 175, sasha);

        savePayment(session, 175, bogdan);
        savePayment(session, 131, bogdan);
        savePayment(session, 171, bogdan);

        savePayment(session, 138, viktor);
        savePayment(session, 131, viktor);
        savePayment(session, 145, viktor);

        savePayment(session, 145, elena);
        savePayment(session, 165, elena);
        savePayment(session, 171, elena);

        savePayment(session, 140, anna);
        savePayment(session, 145, anna);
        savePayment(session, 168, anna);

        savePayment(session, 150, liza);
        savePayment(session, 155, liza);
        savePayment(session, 160, liza);


    }

    private Department saveDepartment(Session session, String name){
        var department = Department.builder()
                .name(name)
                .build();
        session.persist(department);

        return department;
    }

    private Employee saveEmployee(Session session,
                                  String firstName,
                                  String lastName,
                                  LocalDate birthday,
                                  Department department
    ){
        var employee = Employee.builder()
                .employeeInfo(
                        EmployeeInfo.builder()
                                .firstName(firstName)
                                .lastname(lastName)
                                .birthday(birthday)
                                .build())
                .department(department)
                .build();

        session.persist(employee);

        return employee;
    }

    private void savePayment(Session session, Integer amount, Employee employee){
        var payment = Payment.builder()
                .amount(amount)
                .receiver(employee)
                .build();

        session.persist(payment);
    }
}
