package kz.maks.core.back.services.remotes;

import kz.maks.core.back.annotations.Inject;
import kz.maks.core.back.annotations.Remote;
import kz.maks.core.back.services.CoreService;
import kz.maks.core.shared.models.ICombo;
import kz.maks.core.shared.models.ITreeNode;
import kz.maks.core.shared.remotes.CoreRemote;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

@Remote
public class CoreRemoteImpl extends AbstractRemoteImpl implements CoreRemote {

    @Inject(proxy = true)
    private CoreService coreService;

    public CoreRemoteImpl() throws RemoteException {}

    @Override
    public Map<String, List<ITreeNode>> getTrees() throws RemoteException {
        Map<String, List<ITreeNode>> trees = coreService.getTrees();
        return trees;
    }

    @Override
    public Map<String, List<ICombo>> getCombos() throws RemoteException {
        return coreService.getCombos();
    }

}
