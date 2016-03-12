package kz.maks.core.front.services;

import kz.maks.core.shared.remotes.CoreRemote;

public class CoreRemotes {

    private static CoreRemote coreRemote;

    public static CoreRemote coreRemote() {
        return coreRemote;
    }

}
