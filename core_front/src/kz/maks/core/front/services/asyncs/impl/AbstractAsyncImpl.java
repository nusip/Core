package kz.maks.core.front.services.asyncs.impl;

import kz.maks.core.front.services.Callback;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractAsyncImpl {

    private static ExecutorService ASYNC_EXECUTOR = Executors.newCachedThreadPool();

    protected static <RESULT> void executeAsync(final Callable<RESULT> callable, final Callback<RESULT> callback) {
        callback.beforeCall();

        ASYNC_EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {

                boolean wasError = false;

                RESULT result = null;

                try {
                    result = callable.call();

                } catch (Exception e) {
                    wasError = true;
                    callback.onFailure(e);
                }

                if (!wasError) {
                    final RESULT finalResult = result;
//                    try {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(finalResult);
                            }
                        });
//                    } catch (InterruptedException | InvocationTargetException e) {
//                        e.printStackTrace();
//                        throw new RuntimeException(e);
//                    }
                }

                callback.atTheEnd();

            }
        });
    }

}
