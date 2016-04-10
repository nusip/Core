package kz.maks.core.front.ui;

public class Column<T> implements IColumn<T> {
    private final Class<T> tableClass;
    private final String name;
    private final String title;
    private final boolean isEditable;
    private final int width;

    public Column(Class<T> tableClass, String name, String title, boolean isEditable, int width) {
        this.tableClass = tableClass;
        this.name = name;
        this.title = title;
        this.isEditable = isEditable;
        this.width = width;
    }

    @Override
    public Class<T> tableClass() {
        return tableClass;
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
