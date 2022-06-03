package org.example.querying.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.querying.model.Employee;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeDao {

    public static final EmployeeDao INSTANCE = new EmployeeDao();

    public List<Employee> findAll(Session session) {

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);

        JpaRoot<Employee> root = criteria.from(Employee.class);

        criteria.select(root);

        return session.createQuery(criteria)
                .list();
    }

}
