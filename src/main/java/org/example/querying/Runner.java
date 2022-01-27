package org.example.querying;

import lombok.Cleanup;
import org.example.querying.HibernateUtils.SessionUtils;
import org.example.querying.entities.User;
import org.hibernate.Session;

public class Runner {
    public static void main(String[] args) {
        @Cleanup var sessionFactory = SessionUtils.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();


//        var userList = session.createQuery("select u from User u", User.class)
//                .list();
//
//        userList.forEach(u -> u.getPayment().size());
//        userList.forEach(u -> u.getCompany().getName());

        var user = session.get(User.class, 1L);
        user.getPayment().size();

        session.getTransaction().commit();
    }


}
