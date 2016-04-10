package kz.maks.core.shared.models;

import java.io.Serializable;

public class Combo implements ICombo, Serializable {
    private Long id;
    private String title;

    public Combo(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Combo combo = (Combo) o;

        if (!id.equals(combo.id)) return false;
        return title.equals(combo.title);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }
}
