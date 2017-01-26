package workyfie.github.de.workyfie.application.bth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class BTESerachCallback implements BluetoothAdapter.LeScanCallback {
    private IBTHReceiverFoundCallback callback;

    public BTESerachCallback(IBTHReceiverFoundCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        if(bluetoothDevice != null)
            callback.callback(new BthDevice(bluetoothDevice.getAddress(), bluetoothDevice.getName(), bluetoothDevice.getType()));
    }
}
