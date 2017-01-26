package workyfie.github.de.workyfie.presentation.page.main.sensor;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.application.bth.BTESerachCallback;
import workyfie.github.de.workyfie.application.bth.BTHSearchBroadcastReceiver;
import workyfie.github.de.workyfie.application.bitalino.reciever.IBitalinoReceiverStateCallback;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnecting;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.application.bth.BthDevice;
import workyfie.github.de.workyfie.application.bth.IBTHReceiverFoundCallback;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class SensorFragment extends android.support.v4.app.Fragment
        implements SensorView,
        View.OnClickListener{
    public static final String TAG = SensorFragment.class.getSimpleName();

    private final int PERMISSION_REQUEST_COARSE_LOCATION = 2;

    private SensorPresenter presenter;
    private BitalinoReceiveHandler bitalinoReceiveHandler;
    private IBitalinoReceiverStateCallback stateChangeCallback;

    private BTHSearchBroadcastReceiver bthSearchBroadcastReceiver;
    private BTESerachCallback bteSerachCallback;
    private IBTHReceiverFoundCallback foundDeviceCallback;

    private BluetoothAdapter mBluetoothAdapter;

    private ArrayAdapter<BthDevice> adapter;

    private RelativeLayout inforamtionTable;
    private RelativeLayout deviceListContainer;
    private ImageView connectedImage;
    private TextView statusSensor;
    private TextView connectedStatus;
    private TextView emptyList;
    private Button disconnectSensor;
    private Button scanSensor;
    private ListView deviceListView;
    private ProgressBar loaderCycle;

    private AlertDialog alertDialog;

    private boolean tryConnecting;

    public static SensorFragment newInstance() {
        Bundle args = new Bundle();

        SensorFragment fragment = new SensorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<BthDevice> deviceData = new ArrayList<>();
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, deviceData);

        bitalinoReceiveHandler = App.getComponent().getBitalinoReceiveHandler();

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) App.getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        presenter = new SensorPresenter(App.getComponent().getBitalinoProxy(),
                mBluetoothAdapter,
                deviceData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensor_fragment, container, false);

        inforamtionTable = (RelativeLayout) rootView.findViewById(R.id.information_table);
        deviceListContainer = (RelativeLayout) rootView.findViewById(R.id.device_list_container);
        disconnectSensor = (Button) rootView.findViewById(R.id.disconnect_sensor);
        scanSensor = (Button) rootView.findViewById(R.id.scan_sensor);
        statusSensor = (TextView) rootView.findViewById(R.id.sensor_status);
        connectedStatus = (TextView) rootView.findViewById(R.id.textConnected);
        connectedImage = (ImageView) rootView.findViewById(R.id.imageConnectStatus);
        loaderCycle = (ProgressBar) rootView.findViewById(R.id.loader_sensor);
        deviceListView = (ListView) rootView.findViewById(R.id.device_list);
        emptyList = (TextView) rootView.findViewById(R.id.empty_device_list);

        tryConnecting = false;

        stateChangeCallback = state -> presenter.handleBroadcastViewState(state);
        foundDeviceCallback = device -> presenter.handleFoundNewBthDevice(device);

        bthSearchBroadcastReceiver = new BTHSearchBroadcastReceiver(foundDeviceCallback);
        bteSerachCallback = new BTESerachCallback(foundDeviceCallback);

        deviceListView.setAdapter(adapter);

        deviceListView.setOnItemClickListener((adapterView, view, i, l) -> {
            presenter.connect_sensor(getContext(),((BthDevice)deviceListView.getItemAtPosition(i)).adresse, bteSerachCallback);
        });

        permissionCheck();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.attach(this);
        presenter.requestContent();

        Log.i(TAG, "attach");
    }

    @Override
    public void onResume() {
        super.onResume();
        bitalinoReceiveHandler.subscribeState(stateChangeCallback);
        App.getApplication().registerReceiver(bthSearchBroadcastReceiver, bthSearchBroadcastReceiver.getIntentFilter());

        disconnectSensor.setOnClickListener(this);
        scanSensor.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        disconnectSensor.setOnClickListener(null);
        scanSensor.setOnClickListener(null);

        bitalinoReceiveHandler.unsubscribeState(stateChangeCallback);
        App.getApplication().unregisterReceiver(bthSearchBroadcastReceiver);

        presenter.scan_stop(bteSerachCallback);

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "detach");

        presenter.detach();

        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.disconnect_sensor:
                presenter.disconnect_sensor();
                break;
            case R.id.scan_sensor:
                presenter.scan_start(this.getActivity(), bteSerachCallback);
                break;
        }
    }

    @Override
    public void drawState(IBitalinoState state) {
        if (state instanceof BitalinoStateConnected) {
            showIsConnected();
        } else if (state instanceof BitalinoStateDisconnected) {
            showIsDisconnected();
        } else if (state instanceof BitalinoStateRecording) {
            showIsRecording();
        } else if (state instanceof BitalinoStateConnecting){
            showIsConnecting();
        }
    }

    @Override
    public void drawIsScanning(boolean isScanning) {
        if(isScanning)
            showIsScanningDevice();
        else
            showFinishScanningDevice();
    }

    @Override
    public void errMsg(String msg) {
        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(msg)
                .setNeutralButton("Ok", (dialogInterface, i) -> alertDialog.dismiss())
                .create();
        alertDialog.show();
    }

    @Override
    public void notifyDeviceDataChange() {
        adapter.notifyDataSetChanged();
        if(adapter.getCount() > 0){
            emptyList.setVisibility(View.INVISIBLE);
            deviceListView.setVisibility(View.VISIBLE);
        }else{
            emptyList.setVisibility(View.VISIBLE);
            deviceListView.setVisibility(View.INVISIBLE);
        }
    }

    private void showIsConnected() {
        inforamtionTable.setVisibility(View.VISIBLE);

        disconnectSensor.setVisibility(View.VISIBLE);

        scanSensor.setVisibility(View.INVISIBLE);
        deviceListContainer.setVisibility(View.GONE);
        loaderCycle.setVisibility(View.INVISIBLE);

        statusSensor.setText("Verbunden");
        connectedStatus.setText("VERBUNDEN");
        connectedImage.setImageResource(R.mipmap.connected_chains);

        tryConnecting = false;
    }

    private void showIsDisconnected() {
        inforamtionTable.setVisibility(View.GONE);

        disconnectSensor.setVisibility(View.INVISIBLE);

        scanSensor.setEnabled(true);
        scanSensor.setVisibility(View.VISIBLE);
        deviceListContainer.setVisibility(View.VISIBLE);
        loaderCycle.setVisibility(View.INVISIBLE);

        statusSensor.setText("nicht Verbunden");
        connectedStatus.setText("NICHT VERBUNDEN");
        connectedImage.setImageResource(R.mipmap.disconnected_chains);
    }

    private void showIsConnecting(){
        disconnectSensor.setVisibility(View.INVISIBLE);

        scanSensor.setEnabled(false);
        deviceListContainer.setVisibility(View.VISIBLE);
        loaderCycle.setVisibility(View.VISIBLE);

        statusSensor.setText("Verbinde Sensor...");
        connectedStatus.setText("Verbinde...");
        connectedImage.setImageResource(R.mipmap.connecting_dots);

        //handle Timout connecting
        tryConnecting = true;
        new android.os.Handler().postDelayed(
                () -> {
                    if(!presenter.isSensorConnected() && tryConnecting){
                        try {
                            errMsg("Fehler beim Verbinden zum Sensor! Ist der Sensor eingeschaltet?");
                        }catch (NullPointerException e){
                            Log.i(TAG, e.toString());
                        }finally {
                            tryConnecting = false;
                            showIsDisconnected();
                        }
                    }
                },
                15000);

    }

    private void showIsRecording() {
        disconnectSensor.setVisibility(View.VISIBLE);

        scanSensor.setVisibility(View.INVISIBLE);
        deviceListContainer.setVisibility(View.GONE);
        loaderCycle.setVisibility(View.INVISIBLE);

        statusSensor.setText("Zeichne Daten auf...");

        connectedStatus.setText("VERBUNDEN");
        connectedImage.setImageResource(R.mipmap.connected_chains);

    }

    private void showIsScanningDevice(){
        scanSensor.setEnabled(false);
        loaderCycle.setVisibility(View.VISIBLE);
    }
    private void showFinishScanningDevice(){
        scanSensor.setEnabled(true);
        loaderCycle.setVisibility(View.INVISIBLE);
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android Marshmallow and above permission check
            if(this.getActivity().checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setTitle(getString(R.string.permission_check_dialog_title))
                        .setMessage(getString(R.string.permission_check_dialog_message))
                        .setPositiveButton(getString(android.R.string.ok), null)
                        .setOnDismissListener(dialogInterface -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION));
                builder.show();
            }
        }
    }

}
