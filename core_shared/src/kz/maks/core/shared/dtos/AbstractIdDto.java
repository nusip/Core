package kz.maks.core.shared.dtos;

import kz.maks.core.shared.models.HasId;

import java.io.Serializable;

public abstract class AbstractIdDto implements HasId, Serializable {
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
