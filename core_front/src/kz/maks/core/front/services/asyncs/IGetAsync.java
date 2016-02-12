package kz.maks.core.front.services.asyncs;

import kz.maks.core.front.services.Callback;

public interface IGetAsync<GET_TYPE> {

    void get(Long id, Callback<GET_TYPE> callback);

}
