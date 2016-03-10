package kz.maks.core.front.services.asyncs.impl;

import kz.maks.core.front.services.Callback;
import kz.maks.core.front.services.asyncs.CoreAsync;
import kz.maks.core.shared.models.ITreeNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static kz.maks.core.front.services.CoreRemotes.CORE_REMOTE;

public class CoreAsyncImpl extends AbstractAsyncImpl implements CoreAsync {

    @Override
    public void getTrees(Callback<Map<String, List<ITreeNode>>> callback) {
        executeAsync(new Callable<Map<String, List<ITreeNode>>>() {
            @Override
            public Map<String, List<ITreeNode>> call() throws Exception {
                return CORE_REMOTE().getTrees();
            }
        }, callback);
    }

    @Override
    public void login(final String identifier, final String credentials, Callback<Boolean> callback) {
        executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return CORE_REMOTE().login(identifier, credentials);
            }
        }, callback);
    }

}
