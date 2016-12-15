package workyfie.github.de.workyfie.application.bitalino.reciever;

import info.plux.pluxapi.Constants;

public interface IBitalinoReceiverStateCallback {
    void callback(Constants.States state);
}
