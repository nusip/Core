package kz.maks.core.shared.models;

public class Value<T> implements Accessor<T> {
    private T val;

    @Override
    public T get() {
        return val;
    }

    @Override
    public void set(T val) {
        this.val = val;
    }
}
