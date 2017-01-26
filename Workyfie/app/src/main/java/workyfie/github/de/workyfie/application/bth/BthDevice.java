package workyfie.github.de.workyfie.application.bth;

import android.bluetooth.BluetoothDevice;

public class BthDevice {
    public final String adresse;
    public final String name;
    public final int type;

    public BthDevice(String adresse, String name, int type) {
        this.adresse = adresse;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        if(name == null)
            return adresse;
        if(name.isEmpty())
            return adresse;

        return name;
    }
}
