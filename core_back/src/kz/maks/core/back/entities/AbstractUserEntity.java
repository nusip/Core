package kz.maks.core.back.entities;

import kz.maks.core.shared.models.HasIsActive;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AbstractUserEntity extends AbstractBaseEntity implements HasIsActive {
    public static final String ROOT_USER_IDENTIFIER = "root";

    public static final String IDENTIFIER_FIELD = "identifier";
    public static final String CREDENTIALS_FIELD = "credentials";
    public static final String IS_ACTIVE_FIELD = "isActive";

    @Column(unique = true, nullable = false)
    protected String identifier;
    protected String credentials;

    protected Boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    protected List<Role> roles = new ArrayList<>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Transient
    @Override
    public boolean isActive() {
        return Boolean.TRUE.equals(getIsActive());
    }
}
