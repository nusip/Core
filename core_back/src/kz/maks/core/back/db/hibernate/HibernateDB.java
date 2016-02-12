package kz.maks.core.back.db.hibernate;

import kz.maks.core.back.annotations.Bean;
import kz.maks.core.back.annotations.Inject;
import kz.maks.core.back.db.DB;
import kz.maks.core.back.entities.AbstractBaseEntity;
import org.hibernate.SessionFactory;

@Bean
public class HibernateDB implements DB {

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public <ENTITY extends AbstractBaseEntity> ENTITY load(Class<ENTITY> entityClass, long id) {
        return sessionFactory.getCurrentSession().load(entityClass, id);
    }

    @Override
    public <ENTITY extends AbstractBaseEntity> void save(ENTITY entity) {
        if (entity.getId() == null) {
            sessionFactory.getCurrentSession().persist(entity);
        } else {
            sessionFactory.getCurrentSession().merge(entity);
        }
    }

    @Override
    public <ENTITY extends AbstractBaseEntity> void delete(ENTITY entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public <ENTITY extends AbstractBaseEntity> void delete(Class<ENTITY> entityClass, long id) {
        ENTITY entity = load(entityClass, id);
        sessionFactory.getCurrentSession().delete(entity);
    }

}
