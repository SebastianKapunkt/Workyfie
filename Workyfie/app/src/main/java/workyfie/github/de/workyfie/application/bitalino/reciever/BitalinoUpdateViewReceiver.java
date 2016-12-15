package workyfie.github.de.workyfie.application.bitalino.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import info.plux.pluxapi.Constants;
import info.plux.pluxapi.bitalino.BITalinoFrame;

import static info.plux.pluxapi.Constants.ACTION_DATA_AVAILABLE;
import static info.plux.pluxapi.Constants.ACTION_STATE_CHANGED;
import static info.plux.pluxapi.Constants.EXTRA_DATA;
import static info.plux.pluxapi.Constants.EXTRA_STATE_CHANGED;

public class BitalinoUpdateViewReceiver extends BroadcastReceiver {
    private static final String TAG = BitalinoUpdateViewReceiver.class.getSimpleName();

    private IntentFilter intentFilter;
    private IBitalinoReceiverDataCallback callbackData;
    private IBitalinoReceiverStateCallback callbackState;

    public BitalinoUpdateViewReceiver(IBitalinoReceiverStateCallback callbackState,
                                      IBitalinoReceiverDataCallback callbackData){
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATE_CHANGED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);

        this.callbackData = callbackData;
        this.callbackState = callbackState;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_STATE_CHANGED.equals(action)) {
            Constants.States state = Constants.States.getStates(intent.getIntExtra(EXTRA_STATE_CHANGED, 0));

            callbackState.callback(state);

        } else if (ACTION_DATA_AVAILABLE.equals(action)) {
            if (intent.hasExtra(EXTRA_DATA)) {
                Parcelable parcelable = intent.getParcelableExtra(EXTRA_DATA);
                if (parcelable.getClass().equals(BITalinoFrame.class)) { //BITalino
                    BITalinoFrame frame = (BITalinoFrame) parcelable;

                    callbackData.callback(frame);
                }
            }
        }
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
