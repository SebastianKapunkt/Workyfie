package workyfie.github.de.workyfie.application.bitalino;

import android.content.Context;

import info.plux.pluxapi.Communication;
import info.plux.pluxapi.bitalino.BITalinoCommunication;
import info.plux.pluxapi.bitalino.BITalinoCommunicationFactory;
import info.plux.pluxapi.bitalino.BITalinoException;
import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.EBitalinoStateException;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;

public class BitalinoProxy {
    private static final String TAG = BitalinoProxy.class.getSimpleName();

    private IBitalinoState bitalinoState;
    private BITalinoCommunication biTalinoCommunication;

    private String lastMacAdresse;

    public BitalinoProxy() {
        this.biTalinoCommunication = null;
        bitalinoState = new BitalinoStateDisconnected();
    }

    public void initBitalinoCommunicationBLE(Context context) {
        this.initBitalinoCommunication(context, Communication.BLE);
    }

    public void initBitalinoCommunicationBHE(Context context) {
        this.initBitalinoCommunication(context, Communication.BTH);
    }

    public void setState(IBitalinoState state) {
        this.bitalinoState = state;
    }

    public IBitalinoState getBitalinoState() {
        return this.bitalinoState;
    }

    public boolean connect_sensor(){
        return connect_sensor(lastMacAdresse);
    }
    public boolean connect_sensor(String macAdresse) {
        try {
            bitalinoState.connect(biTalinoCommunication, macAdresse);
            lastMacAdresse = macAdresse;
            return true;
        } catch (EBitalinoStateException e) {
            e.printStackTrace();
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean start_recording(int[] channels, int samplerate) {
        try {
            bitalinoState.start(biTalinoCommunication, channels, samplerate);
            return true;
        } catch (EBitalinoStateException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean stop_recording() {
        try {
            bitalinoState.stop(biTalinoCommunication);
            return true;
        } catch (EBitalinoStateException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect_sensor() {
        try {
            bitalinoState.disconnect(biTalinoCommunication);
        } catch (EBitalinoStateException e) {
            e.printStackTrace();
        }

    }

    private void initBitalinoCommunication(Context context, Communication communication) {
        biTalinoCommunication = new BITalinoCommunicationFactory().getCommunication(communication, context);
        bitalinoState = new BitalinoStateDisconnected();
    }


}
