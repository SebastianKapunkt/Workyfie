package workyfie.github.de.workyfie.presentation.page.sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import info.plux.pluxapi.Communication;
import info.plux.pluxapi.Constants;
import info.plux.pluxapi.bitalino.BITalinoCommunication;
import info.plux.pluxapi.bitalino.BITalinoCommunicationFactory;
import info.plux.pluxapi.bitalino.BITalinoDescription;
import info.plux.pluxapi.bitalino.BITalinoException;
import info.plux.pluxapi.bitalino.BITalinoFrame;
import info.plux.pluxapi.bitalino.BITalinoState;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

import static info.plux.pluxapi.Constants.ACTION_COMMAND_REPLY;
import static info.plux.pluxapi.Constants.ACTION_DATA_AVAILABLE;
import static info.plux.pluxapi.Constants.ACTION_DEVICE_READY;
import static info.plux.pluxapi.Constants.ACTION_EVENT_AVAILABLE;
import static info.plux.pluxapi.Constants.ACTION_STATE_CHANGED;
import static info.plux.pluxapi.Constants.EXTRA_COMMAND_REPLY;
import static info.plux.pluxapi.Constants.EXTRA_DATA;
import static info.plux.pluxapi.Constants.EXTRA_STATE_CHANGED;
import static info.plux.pluxapi.Constants.IDENTIFIER;

public class SensorPresenter implements Presenter<SensorView> {
    private final String MAC_ADRESS = "B0:B4:48:F0:C6:8A";
    private final int CHANNEL = 0; //A1 --> 0 A2 --> 1

    private SensorView view;
    private BITalinoCommunication bitalino;

    private boolean isRecording;

    public SensorPresenter(){
        isRecording = false;
    }

    @Override
    public void attach(SensorView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    public void connect_sensor(){
        //TODO do magic to connect the sensor
        try {
            bitalino = new BITalinoCommunicationFactory().getCommunication(Communication.BLE, view.getBaseContext());
            bitalino.connect(MAC_ADRESS);
            view.getActivity().registerReceiver(updateReceiver, makeUpdateIntentFilter());

            view.connected();
        } catch (BITalinoException e) {
            view.err("Fehler beim Verbinden mit dem Sensor");
            e.printStackTrace();
        }
    }
    private void startRecording(){
        isRecording = true;
        view.showIsRecording();
    }
    private void stopRecording(){
        isRecording = false;
        view.showNoRecording();
    }

    public void toogleRecord(){
        if(isRecording){
            try {
                bitalino.stop();
                stopRecording();
            } catch (BITalinoException e) {
                view.err("Fehler beim stoppen der Aufzeichnung");
                e.printStackTrace();
            }
        }else{
            try {
                bitalino.start(new int[]{CHANNEL}, 1000);
                startRecording();
            } catch (BITalinoException e) {
                view.err("Fehler beim starten der Aufzeichnung");
                e.printStackTrace();
            }
        }
    }

    /*
 * Local Broadcast
 */
    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(ACTION_STATE_CHANGED.equals(action)){
                String identifier = intent.getStringExtra(IDENTIFIER);
                Constants.States state = Constants.States.getStates(intent.getIntExtra(EXTRA_STATE_CHANGED, 0));

                view.setStatusSensor(state);

                switch (state){
                    case NO_CONNECTION:
                        break;
                    case LISTEN:
                        break;
                    case CONNECTING:
                        break;
                    case CONNECTED:
                        break;
                    case ACQUISITION_TRYING:
                        break;
                    case ACQUISITION_OK:
                        break;
                    case ACQUISITION_STOPPING:
                        break;
                    case DISCONNECTED:
                        break;
                    case ENDED:
                        break;

                }
            }
            else if(ACTION_DATA_AVAILABLE.equals(action)){
                if(intent.hasExtra(EXTRA_DATA)){
                    Parcelable parcelable = intent.getParcelableExtra(EXTRA_DATA);
                    if(parcelable.getClass().equals(BITalinoFrame.class)){ //BITalino
                        BITalinoFrame frame = (BITalinoFrame) parcelable;
                        view.appendSensorData("AnalogSignal: " + frame.getAnalog(CHANNEL) +  "\n");
                    }
                }
            }
            else if(ACTION_COMMAND_REPLY.equals(action)){
                if(intent.hasExtra(EXTRA_COMMAND_REPLY) && (intent.getParcelableExtra(EXTRA_COMMAND_REPLY) != null)){
                    Parcelable parcelable = intent.getParcelableExtra(EXTRA_COMMAND_REPLY);
                    if(parcelable.getClass().equals(BITalinoState.class)){ //BITalino
                        //view.appendSensorData(parcelable.toString() + "\n");
                    }
                    else if(parcelable.getClass().equals(BITalinoDescription.class)){ //BITalino
                        //view.appendSensorData("isBITalino2: " + ((BITalinoDescription)parcelable).isBITalino2() + "; FwVersion: " + String.valueOf(((BITalinoDescription)parcelable).getFwVersion()) + "\n");
                    }
                }
            }
        }
    };

    private IntentFilter makeUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATE_CHANGED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        intentFilter.addAction(ACTION_EVENT_AVAILABLE);
        intentFilter.addAction(ACTION_DEVICE_READY);
        intentFilter.addAction(ACTION_COMMAND_REPLY);
        return intentFilter;
    }
}
