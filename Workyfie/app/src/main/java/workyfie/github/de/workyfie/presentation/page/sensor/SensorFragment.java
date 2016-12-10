package workyfie.github.de.workyfie.presentation.page.sensor;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.IntentFilter;
import android.os.*;
import android.widget.TextView;

import info.plux.pluxapi.bitalino.*;
import static info.plux.pluxapi.Constants.*;

import info.plux.pluxapi.bitalino.bth.OnBITalinoDataAvailable;
import workyfie.github.de.workyfie.R;

/**
 * Created by insanemac on 10.12.16.
 */

public class SensorFragment extends android.support.v4.app.Fragment implements SensorView{
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

        presenter = new SensorPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sensor_fragment, container, false);

        initView(rootView);

        setUiElements();

        setListener();

        return rootView;
    }

    private void initView(View rootView){
        connectSenor = (Button) rootView.findViewById(R.id.connect_sensor);
        toogleRecord = (Button) rootView.findViewById(R.id.toogle_record);
        statusSensor = (TextView) rootView.findViewById(R.id.status_sensor);
        resultSensor = (TextView) rootView.findViewById(R.id.sensor_results);
    }

    private void setUiElements(){
        connectSenor.setVisibility(View.VISIBLE);
        toogleRecord.setVisibility(View.INVISIBLE);

        statusSensor.setText(currentState.name());
        resultSensor.setVisibility(View.INVISIBLE);
    }

    private void setListener(){
        connectSenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.connect_sensor();
            }
        });
        toogleRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toogleRecord();
            }
        });
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
    public Context getBaseContext() {
        return this.getActivity().getBaseContext();
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
        Log.i(TAG, "BITalinoSensor: "  + state.name());
        statusSensor.setText("BITalinoSensor: "  + state.name());
    }

    @Override
    public void appendSensorData(String data) {
        Log.d(TAG, "BITalinoFrame: " + data);
        resultSensor.setText("BITalinoFrame: " + data + "\n");
    }
}
