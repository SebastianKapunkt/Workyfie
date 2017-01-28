package workyfie.github.de.workyfie.presentation.page.main.measure;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.R;
import workyfie.github.de.workyfie.application.bitalino.config.BitalinoConfig;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.application.bitalino.reciever.IBitalinoReceiverDataCallback;
import workyfie.github.de.workyfie.application.bitalino.reciever.IBitalinoReceiverStateCallback;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnecting;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.presentation.page.main.MainActivity;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarFragment;
import workyfie.github.de.workyfie.presentation.page.main.sidebar.SidebarItem;

public class MeasureFragment extends Fragment implements MeasureView, View.OnClickListener {

    public static final String TAG = MeasureFragment.class.getSimpleName();



    private MeasurePresenter presenter;
    private BitalinoReceiveHandler bitalinoReceiveHandler;
    private IBitalinoReceiverStateCallback stateChangeCallback;
    private IBitalinoReceiverDataCallback dataNewCallback;

    private RelativeLayout sessionTable;
    private RelativeLayout infoNoConnectContainer;
    private GraphView graphView;
    private Button startRecord;
    private Button stopRecord;
    private Button changeToSensor;
    private TextView infoNoConnect;

    private AlertDialog alertDialog;

    private LineGraphSeries<DataPoint> series;
    private final int MAX_X = 40;


    public static MeasureFragment newInstance() {
        Bundle args = new Bundle();

        MeasureFragment fragment = new MeasureFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bitalinoReceiveHandler = App.getComponent().getBitalinoReceiveHandler();

        presenter = new MeasurePresenter(App.getComponent().getThreadingModule(),
                App.getComponent().getBitalinoProxy(),
                App.getComponent().getSessionRepository(),
                bitalinoReceiveHandler,
                BitalinoConfig.BITALINO_CONFIG_EEG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.measure_fragment, container, false);

        sessionTable = (RelativeLayout) rootView.findViewById(R.id.session_table);
        infoNoConnectContainer = (RelativeLayout) rootView.findViewById(R.id.info_container);
        startRecord = (Button) rootView.findViewById(R.id.start_record);
        stopRecord = (Button) rootView.findViewById(R.id.stop_record);
        changeToSensor = (Button) rootView.findViewById(R.id.change_to_sensor);
        graphView = (GraphView) rootView.findViewById(R.id.graph);
        infoNoConnect = (TextView) rootView.findViewById(R.id.textNoConnected);

        series = new LineGraphSeries<>();
        series.setColor(getResources().getColor(R.color.colorAccentDark));
        series.setAnimated(true);
        series.setThickness(10);

        graphView.addSeries(series);
        graphView.setTitle("EEG Daten");
        graphView.setTitleColor(getResources().getColor(R.color.primaryTextDark));
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(0);
        graphView.getViewport().setMaxX(MAX_X);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(1000);

        String textNoConnect = getString(R.string.text_no_connect);
        infoNoConnect.setText(Html.fromHtml(textNoConnect));

        stateChangeCallback = state -> presenter.handleBroadcastViewState(state);
        dataNewCallback = data -> presenter.handleBroadcastViewData(data);

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
        bitalinoReceiveHandler.subscribeData(dataNewCallback);

        startRecord.setOnClickListener(this);
        stopRecord.setOnClickListener(this);
        changeToSensor.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        startRecord.setOnClickListener(null);
        stopRecord.setOnClickListener(null);
        changeToSensor.setOnClickListener(null);

        bitalinoReceiveHandler.unsubscribeData(dataNewCallback);
        bitalinoReceiveHandler.unsubscribeState(stateChangeCallback);

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
            case R.id.start_record:
                presenter.start_recording();
                break;
            case R.id.stop_record:
                presenter.stop_reording(App.getApplication());
                break;
            case R.id.change_to_sensor:
                ((MainActivity)getActivity()).onSidebarItemClicked(SidebarItem.SENSOR);
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
    public void addGraphData(DataPoint dataPoint) {
        if(dataPoint.getX() <= MAX_X){
            series.appendData(dataPoint, false, 100);
        }else{
            series.appendData(dataPoint, true, 100);
        }

    }

    @Override
    public void setGraphData(DataPoint[] dataPoint) {
        series.resetData(dataPoint);
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

    private void showIsConnected() {
        sessionTable.setVisibility(View.VISIBLE);
        infoNoConnectContainer.setVisibility(View.GONE);

        startRecord.setEnabled(true);
        startRecord.setVisibility(View.VISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);}

    private void showIsDisconnected() {
        sessionTable.setVisibility(View.GONE);
        infoNoConnectContainer.setVisibility(View.VISIBLE);

        startRecord.setEnabled(false);
        startRecord.setVisibility(View.VISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);
    }

    private void showIsConnecting(){
        sessionTable.setVisibility(View.GONE);
        infoNoConnectContainer.setVisibility(View.GONE);

        startRecord.setEnabled(false);
        startRecord.setVisibility(View.VISIBLE);
        stopRecord.setVisibility(View.INVISIBLE);
    }

    private void showIsRecording() {
        sessionTable.setVisibility(View.VISIBLE);
        infoNoConnectContainer.setVisibility(View.GONE);

        startRecord.setVisibility(View.INVISIBLE);
        stopRecord.setVisibility(View.VISIBLE);
    }
}
