package kz.maks.core.front.services.asyncs;

import kz.maks.core.front.services.Callback;

public interface IDeleteAsync {

    void delete(Long id, Callback<Void> callback);

}
