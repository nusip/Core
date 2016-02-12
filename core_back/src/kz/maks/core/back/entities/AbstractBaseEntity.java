package kz.maks.core.back.entities;

import kz.maks.core.shared.models.HasId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractBaseEntity implements HasId {
    @Id
    @GeneratedValue
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
