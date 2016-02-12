package kz.maks.core.front.services.asyncs;

public interface ICRUDAsync<SEARCH_PARAMS, LIST_TYPE, GET_TYPE>
        extends IListAsync<SEARCH_PARAMS, LIST_TYPE>, IGetAsync<GET_TYPE>, ISaveAsync<GET_TYPE>, IDeleteAsync {
}
