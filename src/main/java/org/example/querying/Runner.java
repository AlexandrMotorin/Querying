package org.example.querying;

import lombok.Cleanup;

public class Runner {
    public static void main(String[] args) {
        var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        session.getTransaction().commit();
    }

}
