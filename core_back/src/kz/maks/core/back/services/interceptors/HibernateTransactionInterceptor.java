package kz.maks.core.back.services.interceptors;

import kz.maks.core.back.db.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class HibernateTransactionInterceptor extends TransactionInterceptor {

    private static HibernateTransactionInterceptor instance;

    private SessionFactory sessionFactory = Hibernate.getSessionFactory();

    public HibernateTransactionInterceptor() {}

    public static HibernateTransactionInterceptor getInstance() {
        if (instance == null) {
            instance = new HibernateTransactionInterceptor();
        }
        return instance;
    }

    @Override
    public void beginTransaction() {
        sessionFactory.getCurrentSession().beginTransaction();
    }

    @Override
    public void commitTransaction() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }
}
