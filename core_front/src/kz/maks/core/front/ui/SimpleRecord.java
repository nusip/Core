package kz.maks.core.front.ui;

public class SimpleRecord<T> {

    public static final String FIELD_NAME = "value";

    private T value;

    public SimpleRecord() {}

    public SimpleRecord(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
