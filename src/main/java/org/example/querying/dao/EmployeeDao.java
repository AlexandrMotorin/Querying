package org.example.querying.dao;

import jakarta.persistence.Tuple;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.querying.dto.DepartmentDto;
import org.example.querying.model.Employee;
import org.example.querying.model.Payment;
import org.hibernate.Session;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeDao {

    public static final EmployeeDao INSTANCE = new EmployeeDao();


    public List<Employee> findAll(Session session) {

        return null;
    }

    public List<Employee> findAllByFirstName(Session session, String firstname){

        return null;
    }

    public List<Employee> findLimitedEmployeesOrderedByBirthday(Session session, int limit){



        return null;
    }

    public List<Employee> findAllByDepartmentName(Session session, String departmentName){


         return null;
    }

    public List<Payment> findAllPaymentsByDepartmentName(Session session, String departmentName){
        return null;
    }

    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName){

        return null;
    }

    public List<DepartmentDto> findDepartmentNamesWithAvgUserPaymentsOrderedByDepartmentName(Session session){

        return null;
    }

    /**
     * все сотрудники, средний размер выплат, но только для тех чей размер выплат больше среднего
     * размера выплат всех сотрудников. Сортировка по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session){

        return null;
    }
}
