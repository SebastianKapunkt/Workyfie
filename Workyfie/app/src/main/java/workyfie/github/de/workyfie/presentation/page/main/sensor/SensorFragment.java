package workyfie.github.de.workyfie.presentation.page.main.sensor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoCalcDataReciever;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoUpdateViewReceiver;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnecting;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;

public class SensorFragment extends android.support.v4.app.Fragment
        implements SensorView,
        View.OnClickListener,
        Chronometer.OnChronometerTickListener{
    public static final String TAG = SensorFragment.class.getSimpleName();

    private SensorPresenter presenter;

    private BitalinoUpdateViewReceiver updateReceiverView;
    private BitalinoCalcDataReciever updateReceiverCalcData;

    private TextView statusSensor;
    private GraphView graphView;
    private ListView resultSensorListView;
    private Button connectSenor;
    private Button disconnectSensor;
    private Button startRecord;
    private Button stopRecord;
    private Chronometer chrono;

    private AlertDialog alertDialog;
    private ArrayAdapter<SensorData> adapter;
    private LineGraphSeries<DataPoint> series;

    private List<SensorData> resultSensorData;

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

        presenter = new SensorPresenter(
                App.getComponent().getThreadingModule(),
                App.getComponent().getSensorDataRepository(),
                App.getComponent().getBitalinoProxy(),
                App.getComponent().getSessionRepository());

        resultSensorData = new ArrayList<>();
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, resultSensorData);

        updateReceiverView = new BitalinoUpdateViewReceiver(
                state -> presenter.handleBroadcastViewState(state),
                frame -> presenter.handleBroadcastViewData(frame));

        updateReceiverCalcData = new BitalinoCalcDataReciever(
                frame -> presenter.handleBroadcastCalcData(frame));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensor_fragment, container, false);

        connectSenor = (Button) rootView.findViewById(R.id.connect_sensor);
        disconnectSensor = (Button) rootView.findViewById(R.id.disconnect_sensor);
        startRecord = (Button) rootView.findViewById(R.id.start_record);
        stopRecord = (Button) rootView.findViewById(R.id.stop_record);
        graphView = (GraphView) rootView.findViewById(R.id.graph);
        statusSensor = (TextView) rootView.findViewById(R.id.status_sensor);
        chrono = (Chronometer) rootView.findViewById(R.id.chrono);
        resultSensorListView = (ListView) rootView.findViewById(R.id.result_list);

        resultSensorListView.setAdapter(adapter);

        series = new LineGraphSeries<>();

        graphView.addSeries(series);
        graphView.setTitle("Hello");
        graphView.setTitleColor(R.color.green);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);

        tryConnecting = false;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.attach(this);
        presenter.requestContent();

        registerRecieverCalcData();

        Log.i(TAG, "attach");
    }

    @Override
    public void onResume() {
        super.onResume();

        registerRecieverView();

        connectSenor.setOnClickListener(this);
        disconnectSensor.setOnClickListener(this);
        startRecord.setOnClickListener(this);
        stopRecord.setOnClickListener(this);
        chrono.setOnChronometerTickListener(this);
    }

    @Override
    public void onPause() {
        unregisterReceiverView();

        connectSenor.setOnClickListener(null);
        disconnectSensor.setOnClickListener(null);
        startRecord.setOnClickListener(null);
        stopRecord.setOnClickListener(null);
        chrono.setOnChronometerTickListener(null);

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "detach");

        presenter.detach();

        super.onStop();
    }

    @Override
    public void onDestroy(){
        unregisterReceiverCalcData();

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_sensor:
                presenter.connect_sensor(getContext());
                break;
            case R.id.disconnect_sensor:
                presenter.disconnect_sensor();
                break;
            case R.id.start_record:
                presenter.start_recording();
                break;
            case R.id.stop_record:
                presenter.stop_reording(getContext());
                break;
        }
    }

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

    public void drawData(SensorData data) {
        resultSensorData.add(0, data);
        adapter.notifyDataSetChanged();
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
    public void onChronometerTick(Chronometer chronometer) {
        presenter.drawGraph();
    }

    @Override
    public void addGraphData(DataPoint dataPoint) {
        series.appendData(dataPoint, true, 100);
    }

    public void registerRecieverView() {
        getActivity().registerReceiver(updateReceiverView, updateReceiverView.getIntentFilter());
    }

    public void unregisterReceiverView() {
        getActivity().unregisterReceiver(updateReceiverView);
    }

    public void registerRecieverCalcData() {
        getActivity().registerReceiver(updateReceiverCalcData, updateReceiverCalcData.getIntentFilter());
    }

    public void unregisterReceiverCalcData() {
        getActivity().unregisterReceiver(updateReceiverCalcData);
    }

    private void showIsConnected() {
        connectSenor.setVisibility(View.INVISIBLE);
        disconnectSensor.setVisibility(View.VISIBLE);
        startRecord.setVisibility(View.VISIBLE);

        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("VERBUNDEN");
        resultSensorListView.setVisibility(View.VISIBLE);

        tryConnecting = false;
    }

    private void showIsDisconnected() {
        connectSenor.setVisibility(View.VISIBLE);
        connectSenor.setEnabled(true);
        disconnectSensor.setVisibility(View.INVISIBLE);
        startRecord.setVisibility(View.INVISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);
        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("NICHT VERBUNDEN");
        resultSensorListView.setVisibility(View.INVISIBLE);
    }
    private void showIsConnecting(){
        connectSenor.setEnabled(false);
        disconnectSensor.setVisibility(View.INVISIBLE);
        startRecord.setVisibility(View.INVISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);
        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("Verbinde Sensor....");
        resultSensorListView.setVisibility(View.INVISIBLE);

        //handle Timout connecting
        tryConnecting = true;
        new android.os.Handler().postDelayed(
                () -> {
                    if(!presenter.isSensorConnected() && tryConnecting){
                        errMsg("Fehler beim Verbinden zum Sensor! Ist der Sensor eingeschaltet?");
                        tryConnecting = false;
                        showIsDisconnected();
                    }
                },
                15000);

    }

    private void showIsRecording() {
        connectSenor.setVisibility(View.INVISIBLE);
        disconnectSensor.setVisibility(View.VISIBLE);
        startRecord.setVisibility(View.INVISIBLE);
        stopRecord.setVisibility(View.VISIBLE);
        statusSensor.setVisibility(View.VISIBLE);
        statusSensor.setText("RECORDING");
        resultSensorListView.setVisibility(View.VISIBLE);
        chrono.start();
    }
}
