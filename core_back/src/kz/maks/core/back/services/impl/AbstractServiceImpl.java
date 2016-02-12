package kz.maks.core.back.services.impl;

import kz.maks.core.back.annotations.Inject;
import kz.maks.core.back.db.DB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractServiceImpl {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    protected DB db;

    protected Session session() {
        return sessionFactory.getCurrentSession();
    }

}
