package kz.maks.core.front.ui;

public class SimpleRecord {

    public static final String FIELD_NAME = "value";

    private String value;

    public SimpleRecord() {}

    public SimpleRecord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
