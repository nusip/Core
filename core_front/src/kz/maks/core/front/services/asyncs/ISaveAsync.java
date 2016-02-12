package kz.maks.core.front.services.asyncs;

import kz.maks.core.front.services.Callback;

public interface ISaveAsync<SAVE_TYPE> {

    void save(SAVE_TYPE saveType, Callback<Void> callback);

}
