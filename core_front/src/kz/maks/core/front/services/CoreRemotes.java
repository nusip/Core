package kz.maks.core.front.services;

import kz.maks.core.shared.remotes.CoreRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class CoreRemotes {

    private static CoreRemote CORE_REMOTE;

    public static CoreRemote CORE_REMOTE() {
        return CORE_REMOTE;
    }

}
