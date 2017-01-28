package workyfie.github.de.workyfie.presentation.page.main.sensor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import info.plux.pluxapi.Communication;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.application.bth.BTESerachCallback;
import workyfie.github.de.workyfie.application.bth.BthDevice;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class SensorPresenter implements Presenter<SensorView> {
    public static final String TAG = SensorPresenter.class.getSimpleName();

    private final int REQUEST_ENABLE_BT = 1;
    private final long SCAN_PERIOD = 6000;

    private SensorView view;

    private BitalinoProxy bitalino;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BthDevice> deviceList;

    private boolean mScanning;
    private Handler mHandler;

    public SensorPresenter(BitalinoProxy bitalino, BluetoothAdapter bluetoothAdapter,ArrayList<BthDevice> deviceList) {
        this.bitalino = bitalino;
        this.bluetoothAdapter = bluetoothAdapter;
        this.deviceList = deviceList;

        mHandler = new Handler();
    }

    @Override
    public void attach(SensorView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    public void requestContent() {
        drawView(bitalino.getBitalinoState());

    }

    public boolean isSensorConnected() {
        return (bitalino.getBitalinoState() instanceof BitalinoStateConnected || bitalino.getBitalinoState() instanceof BitalinoStateRecording);
    }

    public void scan_start(Activity activity,BTESerachCallback serachCallback){

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        deviceList.clear();
        view.notifyDeviceDataChange();
        scanDevice(true, serachCallback);

    }
    public void scan_stop(BTESerachCallback serachCallback){
        scanDevice(false, serachCallback);
    }

    private void scanDevice(final boolean enable, BTESerachCallback serachCallback){
        if(enable){
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(() -> {
                bluetoothAdapter.cancelDiscovery();
                bluetoothAdapter.startLeScan(serachCallback);
                mHandler.postDelayed(() -> {
                    bluetoothAdapter.stopLeScan(serachCallback);
                    drawViewScanning(false);
                }, SCAN_PERIOD);
            }, SCAN_PERIOD);
            drawViewScanning(true);
            bluetoothAdapter.startDiscovery();
        }else{
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.stopLeScan(serachCallback);
            drawViewScanning(false);
        }
    }

    public void connect_sensor(Context context, String adresse, int type, BTESerachCallback serachCallback) {
        scanDevice(false, serachCallback);

        Communication communication = Communication.getById(type);
        if(communication.equals(Communication.DUAL) || communication.equals(Communication.BLE)){
            bitalino.initBitalinoCommunicationBLE(context);
        }
        else if (communication.equals(Communication.BTH)) {
            bitalino.initBitalinoCommunicationBTH(context);
        }else{
            Log.e(TAG, "unsupportd/unknow bluetooth connection");
            view.errMsg("Fehler beim Verbinden mit dem Sensor.");
            return;
        }

        if (!bitalino.connect_sensor(adresse)) {
            view.errMsg("Fehler beim Verbinden mit dem Sensor. Ist BT eingeschaltet?");
        }
    }

    public void disconnect_sensor() {
        bitalino.disconnect_sensor();
    }

    private void drawView(IBitalinoState state) {
        view.drawState(state);
    }

    private void drawViewScanning(boolean isScanning){view.drawIsScanning(isScanning);}

    public void handleBroadcastViewState(IBitalinoState state) {
        drawView(state);
    }

    public void handleFoundNewBthDevice(BthDevice device){
        for (BthDevice otherDevice: deviceList) {
            if(otherDevice.adresse.equals(device.adresse))
                return;
        }
        deviceList.add(device);
        view.notifyDeviceDataChange();
    }
}
