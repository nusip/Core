package kz.maks.core.shared.models;

import java.io.Serializable;
import java.util.List;

public class ListResponse<RECORD> implements Serializable {
    private List<RECORD> list;
    private boolean hasNext;

    public ListResponse(List<RECORD> list, boolean hasNext) {
        this.list = list;
        this.hasNext = hasNext;
    }

    public List<RECORD> getList() {
        return list;
    }

    public void setList(List<RECORD> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
