package kz.maks.core.front.services.asyncs;

import kz.maks.core.front.services.Callback;
import kz.maks.core.shared.models.ListResponse;

import java.util.List;

public interface IListAsync<PARAMS, LIST_TYPE> {

    void list(PARAMS params, Callback<ListResponse<LIST_TYPE>> callback);

}
