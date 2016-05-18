package kz.maks.core.back.services.interceptors;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TracingInterceptor implements Interceptor {

    private static final String TRACE_FORMAT_START = "TRACE [%d] %s %s.%s() START";
    private static final String TRACE_FORMAT_END = "TRACE [%d] %s %s.%s() END";

    private static TracingInterceptor instance;

    private static Logger log = Logger.getLogger(TracingInterceptor.class);

    private TracingInterceptor() {}

    public static TracingInterceptor getInstance() {
        if (instance == null) {
            instance = new TracingInterceptor();
        }
        return instance;
    }

    @Override
    public void executeBefore(Object obj, Method method, Object[] args) {
        String sDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String trace = String.format(TRACE_FORMAT_START,
                Thread.currentThread().getId(), sDateTime, obj.getClass().getName(), method.getName());
        log.info(trace);
    }

    @Override
    public void executeAfter(Object obj, Method method, Object[] args) {
        String sDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String trace = String.format(TRACE_FORMAT_END,
                Thread.currentThread().getId(), sDateTime, obj.getClass().getName(), method.getName());
        log.info(trace);
    }

}
