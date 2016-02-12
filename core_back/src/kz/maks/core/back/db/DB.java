package kz.maks.core.back.db;

import kz.maks.core.back.entities.AbstractBaseEntity;

public interface DB {

    <ENTITY extends AbstractBaseEntity> ENTITY load(Class<ENTITY> entityClass, long id);

    <ENTITY extends AbstractBaseEntity> void save(ENTITY entity);

    <ENTITY extends AbstractBaseEntity> void delete(ENTITY entity);

    <ENTITY extends AbstractBaseEntity> void delete(Class<ENTITY> entityClass, long id);

}
