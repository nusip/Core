package kz.maks.core.back.db.hibernate;

import org.hibernate.SessionFactory;

public class Hibernate {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        if (Hibernate.sessionFactory != null) {
            throw new RuntimeException("SessionFactory cannot be set twice");
        }
        Hibernate.sessionFactory = sessionFactory;
    }

}
