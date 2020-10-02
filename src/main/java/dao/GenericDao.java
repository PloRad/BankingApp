package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class GenericDao<T> {

    protected SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void saveEntity(T entity) {

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
    }

    public void updateEntity(T entity) {

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        session.close();
    }

    public void updateEntity(T entity1, T entity2) {

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        session.update(entity1);
        session.update(entity2);
        transaction.commit();
        session.close();
    }

    public T find(Class<T> tClass, long id) {

        Session session = sessionFactory.openSession();
        return session.find(tClass, id);
    }
}
