package kz.maks.core.front.services.asyncs;

import kz.maks.core.front.services.Callback;
import kz.maks.core.shared.models.ITreeNode;

import java.util.List;
import java.util.Map;

public interface CoreAsync {

    void getTrees(Callback<Map<String, List<ITreeNode>>> callback);

}
