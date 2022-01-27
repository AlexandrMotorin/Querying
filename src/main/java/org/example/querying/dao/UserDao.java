package org.example.querying.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.querying.dto.CompanyDto;
import org.example.querying.entities.*;
import org.hibernate.Session;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public List<User> findAll(Session session) {
        //var userList = session.createQuery("from User", User.class).getResultList();

        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(User.class);

        var user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    public List<User> findAllByFirstname(Session session, String firstName){

//        var resultList = session.createQuery("from User u where u.personalInfo.firstname =?1", User.class)
//                .setParameter(1, firstName)
//                .getResultList();


        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);

        var user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(user.get("personalInfo").get("firstname"),firstName)
        );

        return session.createQuery(criteria).list();
    }
    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
//        var resultList = session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
//                .setMaxResults(limit)
//                .list();
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.birthDate)));

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
//        var resultList = session.createQuery("select u from Company c " +
//                        "join c.users u " +
//                        "where c.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var company = criteria.from(Company.class);
        var users = company.join(Company_.users);

        criteria.select(users).where(
                cb.equal(company.get(Company_.name), companyName)
        );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {

//        var paymentList = session.createQuery("select p from Payment p " +
//                        "join p.receiver u " +
//                        "join u.company c " +
//                        "where c.name = :companyName " +
//                        "order by u.personalInfo.firstname, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Payment.class);

        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);
        var company = user.join(User_.company);

        criteria.select(payment).where(
                cb.equal(company.get(Company_.name),companyName)
        )
                .orderBy(
                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.FIRSTNAME)),
                        cb.asc(payment.get(Payment_.amount))
                );

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанным именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
//        var result = session.createQuery("select avg(p.amount) from Payment p " +
//                        "join p.receiver u " +
//                        "where u.personalInfo.firstname = :firstname " +
//                        "and u.personalInfo.lastname = :lastname", Double.class)
//                .setParameter("firstname", firstName)
//                .setParameter("lastname", lastName)
//                .uniqueResult();

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Double.class);

        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName),
                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName)
        );

        return session.createQuery(criteria).uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
//        var list = session.createQuery("select c.name, avg(p.amount) from Company c " +
//                        "join c.users u " +
//                        "join u.payment p " +
//                        "group by c.name " +
//                        "order by c.name", Object[].class)
//                .list();

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CompanyDto.class);

        var company = criteria.from(Company.class);
        var user = company.join(Company_.users);
        var payment = user.join(User_.payment);

        criteria.select(
                cb.construct(CompanyDto.class,
                        company.get(Company_.name),
                        cb.avg(payment.get(Payment_.amount)))
                )
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {

//        var list = session.createQuery("select u, avg(p.amount) from User u " +
//                        "join u.payment p " +
//                        "group by u " +
//                        "having avg(p.amount) > (select avg (p.amount) from Payment p) " +
//                        "order by u.personalInfo.firstname", Object[].class)
//                .list();

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Tuple.class);

        var user = criteria.from(User.class);
        var payment = user.join(User_.payment);

        var subquery = criteria.subquery(Double.class);
        var paymentSubquery = subquery.from(Payment.class);

        criteria.select(
                cb.tuple(
                        user,
                        cb.avg(payment.get(Payment_.amount))
                )
        )
                .groupBy(user.get(User_.id))
                .having(cb.gt(
                        cb.avg(payment.get(Payment_.amount)),
                        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))
                        ))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));


        return session.createQuery(criteria).list();
    }
}
