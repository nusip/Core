package kz.maks.core.back.entities;

import kz.maks.core.shared.models.HasName;
import kz.maks.core.shared.models.ICombo;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@SequenceGenerator(name = "id_gen", sequenceName = "role_seq")
public class Role extends AbstractBaseEntity implements ICombo, HasName {
    @Column(unique = true, nullable = false)
    private String name;
    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Transient
    @Override
    public String name() {
        return getName();
    }

}
