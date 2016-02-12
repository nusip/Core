package kz.maks.core.back.services;

import kz.maks.core.shared.models.ITreeNode;

import java.util.List;
import java.util.Map;

public interface CoreService {

    Map<String, List<ITreeNode>> getTrees();

}
