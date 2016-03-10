package kz.maks.core.back.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUserEntity extends AbstractBaseEntity {
    public static final String IDENTIFIER_FIELD = "identifier";
    public static final String CREDENTIALS_FIELD = "credentials";

    @Column(unique = true, nullable = false)
    protected String identifier;
    protected String credentials;

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
}
