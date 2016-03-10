package kz.maks.core.back.services;

import kz.maks.core.shared.models.Combo;
import kz.maks.core.shared.models.ICombo;
import kz.maks.core.shared.models.ITreeNode;

import java.util.List;
import java.util.Map;

public interface CoreService {

    Map<String, List<ITreeNode>> getTrees();

    boolean login(String identifier, String password);

    Map<String, List<ICombo>> getCombos();

}
