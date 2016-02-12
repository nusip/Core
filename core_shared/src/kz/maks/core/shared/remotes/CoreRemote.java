package kz.maks.core.shared.remotes;

import kz.maks.core.shared.models.ITreeNode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface CoreRemote extends Remote {

    Map<String, List<ITreeNode>> getTrees() throws RemoteException;

}
