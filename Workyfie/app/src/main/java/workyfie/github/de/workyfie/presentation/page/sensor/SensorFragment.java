package workyfie.github.de.workyfie.presentation.page.sensor;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static info.plux.pluxapi.Constants.*;

import info.plux.pluxapi.Constants;
import info.plux.pluxapi.bitalino.BITalinoDescription;
import info.plux.pluxapi.bitalino.BITalinoFrame;
import info.plux.pluxapi.bitalino.BITalinoState;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.WorkyfieFactory;

public class SensorFragment extends android.support.v4.app.Fragment implements SensorView, View.OnClickListener {
    public static final String TAG = SensorFragment.class.getSimpleName();

    private SensorPresenter presenter;
    private AlertDialog alert;
    private TextView statusSensor;
    private TextView resultSensor;
    private Button connectSenor;
    private Button toogleRecord;

    private States currentState = States.DISCONNECTED;

    public static SensorFragment newInstance() {
        Bundle args = new Bundle();

        SensorFragment fragment = new SensorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new SensorPresenter(WorkyfieFactory.newInstance().getSensorDataRe);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensor_fragment, container, false);

        connectSenor = (Button) rootView.findViewById(R.id.connect_sensor);
        toogleRecord = (Button) rootView.findViewById(R.id.toogle_record);
        statusSensor = (TextView) rootView.findViewById(R.id.status_sensor);
        resultSensor = (TextView) rootView.findViewById(R.id.sensor_results);

        connectSenor.setVisibility(View.VISIBLE);
        toogleRecord.setVisibility(View.INVISIBLE);

        statusSensor.setText(currentState.name());
        resultSensor.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attach(this);
        Log.i(TAG, "attach");
    }

    @Override
    public void onStop() {
        Log.i(TAG, "detach");
        presenter.detach();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        connectSenor.setOnClickListener(this);
        toogleRecord.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        connectSenor.setOnClickListener(null);
        toogleRecord.setOnClickListener(null);
        super.onPause();
    }

    @Override
    public void connected() {
        connectSenor.setVisibility(View.INVISIBLE);
        toogleRecord.setVisibility(View.VISIBLE);
        resultSensor.setVisibility(View.VISIBLE);
    }

    @Override
    public void err(String msg) {
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

    @Override
    public void showIsRecording() {
        toogleRecord.setText(R.string.stop_record);

    }

    @Override
    public void showNoRecording() {
        toogleRecord.setText(R.string.start_record);
    }

    @Override
    public void setStatusSensor(States state) {
        Log.i(TAG, "BITalinoSensor: " + state.name());
        statusSensor.setText("BITalinoSensor: " + state.name());
    }

    @Override
    public void appendSensorData(String data) {
        Log.d(TAG, "BITalinoFrame: " + data);
        resultSensor.setText("BITalinoFrame: " + data + "\n");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect_sensor:
                presenter.connect_sensor(getContext());
                break;
            case R.id.toogle_record:
                presenter.toogleRecord();
                break;
        }
    }
}
