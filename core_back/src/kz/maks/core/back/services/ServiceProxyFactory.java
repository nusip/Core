package kz.maks.core.back.services;

import com.google.common.base.Joiner;
import kz.maks.core.back.services.interceptors.Interceptor;
import kz.maks.core.back.services.interceptors.TracingInterceptor;
import kz.maks.core.back.services.interceptors.TransactionInterceptorFactory;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ServiceProxyFactory {

    private static Logger log = Logger.getLogger(ServiceProxyFactory.class);

    private static final Interceptor[] INTERCEPTORS = new Interceptor[] {
            TracingInterceptor.getInstance(),
            TransactionInterceptorFactory.get()
    };

    public static <T> T getProxy(final T obj) {
        return (T) Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        for (int i = 0; i < INTERCEPTORS.length; i++) {
                            INTERCEPTORS[i].executeBefore(obj, method, args);
                        }

                        Object result;

                        try {
                            result = method.invoke(obj, args);

                        } catch (Exception e) {
                            log.error("EXCEPTION IN " + method.getDeclaringClass().getName() + "."
                                    + method.getName() + " with arguments: "
                                    + Joiner.on(", ").useForNull("null").join(args), e);
                            throw e;
                        }

                        for (int i = INTERCEPTORS.length - 1; i >= 0; i--) {
                            INTERCEPTORS[i].executeAfter(obj, method, args);
                        }

                        return result;
                    }
                }
        );
    }

}
