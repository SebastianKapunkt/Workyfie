package workyfie.github.de.workyfie.application.bitalino.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import org.threeten.bp.Instant;

import info.plux.pluxapi.Constants;
import info.plux.pluxapi.bitalino.BITalino;
import info.plux.pluxapi.bitalino.BITalinoFrame;
import workyfie.github.de.workyfie.application.bitalino.config.BitalinoConfig;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnecting;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.EBitalinoStateException;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;

import static info.plux.pluxapi.Constants.ACTION_DATA_AVAILABLE;
import static info.plux.pluxapi.Constants.ACTION_STATE_CHANGED;
import static info.plux.pluxapi.Constants.EXTRA_DATA;
import static info.plux.pluxapi.Constants.EXTRA_STATE_CHANGED;

public class BitalinoBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = BitalinoBroadcastReceiver.class.getSimpleName();

    private IntentFilter intentFilter;
    private IBitalinoOnReceiveCallback callback;
    private BitalinoConfig bitalinoConfig;

    public BitalinoBroadcastReceiver(IBitalinoOnReceiveCallback callback,
                                     BitalinoConfig bitalinoConfig){
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATE_CHANGED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);

        this.callback = callback;
        this.bitalinoConfig = bitalinoConfig;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_STATE_CHANGED.equals(action)) {
            Constants.States state = Constants.States.getStates(intent.getIntExtra(EXTRA_STATE_CHANGED, 0));

            try {
                callback.onReceiveState(createBitalinoState(state));
            } catch (EBitalinoStateException e) {
                e.printStackTrace();
            }

        } else if (ACTION_DATA_AVAILABLE.equals(action)) {
            if (intent.hasExtra(EXTRA_DATA)) {
                Parcelable parcelable = intent.getParcelableExtra(EXTRA_DATA);
                if (parcelable.getClass().equals(BITalinoFrame.class)) { //BITalino
                    BITalinoFrame frame = (BITalinoFrame) parcelable;

                    callback.onReceiveData(createSensorData(frame));
                }
            }
        }
    }

    private IBitalinoState createBitalinoState(Constants.States state) throws EBitalinoStateException {
        switch (state) {
            case NO_CONNECTION:
                return new BitalinoStateDisconnected();
            case LISTEN:
                 return new BitalinoStateConnected();
            case CONNECTING:
                return new BitalinoStateConnecting();
            case CONNECTED:
                return new BitalinoStateConnected();
            case ACQUISITION_TRYING:
                return new BitalinoStateConnected();
            case ACQUISITION_OK:
                return new BitalinoStateRecording();
            case ACQUISITION_STOPPING:
                return new BitalinoStateRecording();
            case DISCONNECTED:
                return new BitalinoStateDisconnected();
            case ENDED:
            default: throw new EBitalinoStateException("Unhandled state:" + state.toString());
        }
    }

    private SensorData createSensorData(BITalinoFrame frame){
        return new SensorData(Integer.valueOf(Instant.now().getNano()).toString(), frame.getAnalog(bitalinoConfig.CHANNEL), System.currentTimeMillis());
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
