package kz.maks.core.back.services.interceptors;

public class TransactionInterceptorFactory {

    public static TransactionInterceptor get() {
        return HibernateTransactionInterceptor.getInstance();
    }

}
