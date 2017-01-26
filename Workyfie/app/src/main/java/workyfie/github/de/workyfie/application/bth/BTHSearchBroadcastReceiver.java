package workyfie.github.de.workyfie.application.bth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BTHSearchBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BTHSearchBroadcastReceiver.class.getSimpleName();
    private IBTHReceiverFoundCallback callback;
    private IntentFilter intentFilter;

    public BTHSearchBroadcastReceiver(IBTHReceiverFoundCallback callback) {
        this.callback = callback;
        this.intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        //Finding devices
        if (BluetoothDevice.ACTION_FOUND.equals(action))
        {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if(device != null)
                callback.callback(new BthDevice(device.getAddress(), device.getName(), device.getType()));
        }
    }
    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
