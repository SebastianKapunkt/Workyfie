package workyfie.github.de.workyfie.application.bitalino.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import info.plux.pluxapi.bitalino.BITalinoFrame;

import static info.plux.pluxapi.Constants.ACTION_DATA_AVAILABLE;
import static info.plux.pluxapi.Constants.EXTRA_DATA;

public class BitalinoCalcDataReciever extends BroadcastReceiver{
    private static final String TAG = BitalinoCalcDataReciever.class.getSimpleName();

    private IntentFilter intentFilter;
    private IBitalinoReceiverDataCallback callbackData;

    public BitalinoCalcDataReciever(IBitalinoReceiverDataCallback callbackData){
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_AVAILABLE);

        this.callbackData = callbackData;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
         if (ACTION_DATA_AVAILABLE.equals(action)) {
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
