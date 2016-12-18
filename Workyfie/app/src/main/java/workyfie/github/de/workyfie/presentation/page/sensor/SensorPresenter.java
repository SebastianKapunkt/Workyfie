package workyfie.github.de.workyfie.presentation.page.sensor;

import android.util.Log;

import info.plux.pluxapi.Constants;
import info.plux.pluxapi.bitalino.BITalinoFrame;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.data.repository.sensordata.SensorDataRepository;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class SensorPresenter implements Presenter<SensorView> {
    public static final String TAG = SensorPresenter.class.getSimpleName();

    private final String MAC_ADRESS = "B0:B4:48:F0:C6:8A";
    private final int CHANNEL = 0; //A1 --> 0 A2 --> 1
    private final int SAMPLE_RATE= 1000;

    private SensorView view;

    private SensorDataRepository repository;
    private BitalinoProxy bitalino;


    public SensorPresenter(SensorDataRepository repository, BitalinoProxy bitalino) {
        this.repository = repository;
        this.bitalino = bitalino;
    }

    @Override
    public void attach(SensorView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    public boolean isRecordingData(){
        return (bitalino.getBitalinoState() instanceof BitalinoStateRecording);
    }
    public void requestContent(){
        drawView(bitalino.getBitalinoState());
    }

    public void connect_sensor() {
        bitalino.initBitalinoCommunicationBLE();
        if( bitalino.connect_sensor(MAC_ADRESS)) {
            view.registerRecieverView();
            view.registerRecieverCalcData();
        }else{
            view.errMsg("Fehler beim Verbinden mit dem Sensor. Ist BT eingeschaltet?");
        }
        drawView(bitalino.getBitalinoState());
    }
    public void start_recording(){
        if( !bitalino.start_recording(new int[] {CHANNEL}, SAMPLE_RATE)){
            view.errMsg("Fehler beim Starten der Aufnahme!");
        }
        drawView(bitalino.getBitalinoState());

    }
    public void stop_reording(){
        if( !bitalino.stop_recording()){
            view.errMsg("Fehler beim Stoppern er Aufnahmen");
        }
        drawView(bitalino.getBitalinoState());
    }
    public void disconnect_sensor(){
        bitalino.disconnect_sensor();

        view.unregisterReceiverView();
        view.unregisterReceiverCalcData();

        drawView(bitalino.getBitalinoState());
    }

    public void handleBroadcastViewState(Constants.States state){
        switch (state){
            case NO_CONNECTION: bitalino.setState(new BitalinoStateDisconnected());
                break;
            case LISTEN: bitalino.setState(new BitalinoStateConnected());
                break;
            case CONNECTING: bitalino.setState(new BitalinoStateConnected());
                break;
            case CONNECTED: bitalino.setState(new BitalinoStateConnected());
                break;
            case ACQUISITION_TRYING: bitalino.setState(new BitalinoStateRecording());
                break;
            case ACQUISITION_OK: bitalino.setState(new BitalinoStateRecording());
                break;
            case ACQUISITION_STOPPING: bitalino.setState(new BitalinoStateRecording());
                break;
            case DISCONNECTED: bitalino.setState(new BitalinoStateDisconnected());
                break;
            case ENDED: bitalino.setState(new BitalinoStateDisconnected());
                break;
        }
        drawView(bitalino.getBitalinoState());

    }

    public void handleBroadcastViewData(BITalinoFrame frame){
        drawView(new SensorData("", frame.getAnalog(CHANNEL), System.currentTimeMillis()));
    }

    public void handleBroadcastCalcData(BITalinoFrame frame){
        //TODO do magic with DATA
        Log.i(TAG, String.valueOf(frame.getAnalog(CHANNEL)));
        repository.add(new SensorData("", frame.getAnalog(CHANNEL), System.currentTimeMillis()));
    }

    private void drawView(IBitalinoState state){
        view.drawState(state);
    }

    private void drawView(SensorData data){
        view.drawData(data);
    }
}
