package workyfie.github.de.workyfie.presentation.page.sensor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.plux.pluxapi.bitalino.BITalinoFrame;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.WorkyfieFactory;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoCalcDataReciever;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoUpdateViewReceiver;
import workyfie.github.de.workyfie.application.bitalino.reciever.IBitalinoReceiverDataCallback;
import workyfie.github.de.workyfie.application.bitalino.reciever.IBitalinoReceiverStateCallback;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;

import static info.plux.pluxapi.Constants.States;

public class SensorFragment extends android.support.v4.app.Fragment implements SensorView, View.OnClickListener {
    public static final String TAG = SensorFragment.class.getSimpleName();

    private SensorPresenter presenter;

    private BitalinoUpdateViewReceiver updateReceiverView;
    private BitalinoCalcDataReciever updateReceiverCalcData;

    private AlertDialog alert;
    private TextView statusSensor;

    private List resultSensorData;
    private ListView resultSensorListView;
    private ArrayAdapter<SensorData> adapter;


    private Button connectSenor;
    private Button disconnectSensor;
    private Button startRecord;
    private Button stopRecord;



    public static SensorFragment newInstance() {
        Bundle args = new Bundle();

        SensorFragment fragment = new SensorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new SensorPresenter(WorkyfieFactory.getInstance().getRepoFactory().getSensorDataRepository(),
                WorkyfieFactory.getInstance().getApplicationFactory().getBitalino());

        resultSensorData = new ArrayList();
        adapter = new ArrayAdapter<SensorData>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, resultSensorData);

        updateReceiverView = new BitalinoUpdateViewReceiver(
                new IBitalinoReceiverStateCallback() {
                    @Override
                    public void callback(States state) {
                        presenter.handleBroadcastViewState(state);
                    }
                },
                new IBitalinoReceiverDataCallback() {
                    @Override
                    public void callback(BITalinoFrame frame) {
                        presenter.handleBroadcastViewData(frame);
                    }
        });
        updateReceiverCalcData = new BitalinoCalcDataReciever(
                new IBitalinoReceiverDataCallback() {
                    @Override
                    public void callback(BITalinoFrame frame) {
                        presenter.handleBroadcastCalcData(frame);
                    }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensor_fragment, container, false);

        connectSenor = (Button) rootView.findViewById(R.id.connect_sensor);
        disconnectSensor = (Button) rootView.findViewById(R.id.disconnect_sensor);
        startRecord = (Button) rootView.findViewById(R.id.start_record);
        stopRecord = (Button) rootView.findViewById(R.id.stop_record);

        statusSensor = (TextView) rootView.findViewById(R.id.status_sensor);
        resultSensorListView = (ListView) rootView.findViewById(R.id.result_list);

        resultSensorListView.setAdapter(adapter);

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

        if(presenter.isRecordingData())
            registerRecieverView();

        connectSenor.setOnClickListener(this);
        disconnectSensor.setOnClickListener(this);
        startRecord.setOnClickListener(this);
        stopRecord.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        if(presenter.isRecordingData())
            unregisterReceiverView();

        connectSenor.setOnClickListener(null);
        disconnectSensor.setOnClickListener(null);
        startRecord.setOnClickListener(null);
        stopRecord.setOnClickListener(null);

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "detach");

        unregisterReceiverView();
        unregisterReceiverCalcData();

        presenter.detach();

        super.onStop();
    }

    @Override
    public void onDestroy(){
        unregisterReceiverView();
        unregisterReceiverCalcData();

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_sensor:
                presenter.connect_sensor();
                break;
            case R.id.disconnect_sensor:
                presenter.disconnect_sensor();
                break;
            case R.id.start_record:
                presenter.start_recording();
                break;
            case R.id.stop_record:
                presenter.stop_reording();
                break;
        }
    }

    public void drawState(IBitalinoState state){
        if(state instanceof BitalinoStateConnected)
            showIsConnected();
        else if(state instanceof BitalinoStateDisconnected)
            showIsDisconnected();
        else if(state instanceof BitalinoStateRecording)
            showIsRecording();
    }
    public void drawData(SensorData data){
        resultSensorData.add(0, data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void errMsg(String msg) {
        alert = new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(msg)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                })
                .create();
        alert.show();
    }

    public void registerRecieverView(){
        getContext().registerReceiver(updateReceiverView, updateReceiverView.getIntentFilter());
    }

    public void unregisterReceiverView(){
        getContext().unregisterReceiver(updateReceiverView);
    }

    public void registerRecieverCalcData(){
        getContext().registerReceiver(updateReceiverCalcData, updateReceiverCalcData.getIntentFilter());
    }

    public void unregisterReceiverCalcData(){
        getContext().unregisterReceiver(updateReceiverCalcData);
    }

    private void showIsConnected(){
        connectSenor.setVisibility(View.INVISIBLE);
        disconnectSensor.setVisibility(View.VISIBLE);
        startRecord.setVisibility(View.VISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);

        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("VERBUNDEN");
        resultSensorListView.setVisibility(View.VISIBLE);
    }

    private void showIsDisconnected(){
        connectSenor.setVisibility(View.VISIBLE);
        disconnectSensor.setVisibility(View.INVISIBLE);
        startRecord.setVisibility(View.INVISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);

        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("NICHT VERBUNDEN");
        resultSensorListView.setVisibility(View.INVISIBLE);
    }

    private void showIsRecording(){
        connectSenor.setVisibility(View.INVISIBLE);
        disconnectSensor.setVisibility(View.VISIBLE);
        startRecord.setVisibility(View.INVISIBLE);
        stopRecord.setVisibility(View.VISIBLE);

        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("RECORDING");
        resultSensorListView.setVisibility(View.VISIBLE);
    }
}
