package kz.maks.core.back.services.interceptors;


import java.lang.reflect.Method;

public abstract class TransactionInterceptor implements Interceptor {

    @Override
    public void executeBefore(Object obj, Method method, Object[] args) {
        beginTransaction();
    }

    @Override
    public void executeAfter(Object obj, Method method, Object[] args) {
        commitTransaction();
    }

    public abstract void beginTransaction();

    public abstract void commitTransaction();

}
