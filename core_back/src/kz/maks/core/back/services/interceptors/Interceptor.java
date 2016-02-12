package kz.maks.core.back.services.interceptors;

import java.lang.reflect.Method;

public interface Interceptor {

    void executeBefore(Object obj, Method method, Object[] args);

    void executeAfter(Object obj, Method method, Object[] args);

}
