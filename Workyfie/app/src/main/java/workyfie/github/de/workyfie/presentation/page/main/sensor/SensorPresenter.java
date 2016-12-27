package workyfie.github.de.workyfie.presentation.page.main.sensor;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import info.plux.pluxapi.Constants;
import info.plux.pluxapi.bitalino.BITalinoFrame;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateDisconnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.data.repos.sensordata.SensorDataRepository;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class SensorPresenter implements Presenter<SensorView> {
    public static final String TAG = SensorPresenter.class.getSimpleName();

    private final String MAC_ADRESS = "B0:B4:48:F0:CE:CA";
    private final int CHANNEL = 0; //A1 --> 0 A2 --> 1 "BO:B4:48:F0:CE:CA"
    private final int SAMPLE_RATE = 1000;

    private SensorView view;
    private int id = 0;
    private int x = 0;
    private SensorDataRepository repository;
    private BitalinoProxy bitalino;

    private CompositeSubscription subscription;
    private final ThreadingModule threadingModule;

    public SensorPresenter(ThreadingModule threadingModule, SensorDataRepository repository, BitalinoProxy bitalino) {
        this.threadingModule = threadingModule;
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
        subscription.unsubscribe();
        subscription = null;
    }

    private CompositeSubscription subscription() {
        if (subscription == null) {
            subscription = new CompositeSubscription();
        }
        return subscription;
    }

    public boolean isRecordingData() {
        return (bitalino.getBitalinoState() instanceof BitalinoStateRecording);
    }

    public void requestContent() {
        drawView(bitalino.getBitalinoState());
    }

    public void connect_sensor(Context context) {
        bitalino.initBitalinoCommunicationBLE(context);
        if (bitalino.connect_sensor(MAC_ADRESS)) {
            view.registerRecieverView();
            view.registerRecieverCalcData();
        } else {
            view.errMsg("Fehler beim Verbinden mit dem Sensor. Ist BT eingeschaltet?");
        }
        drawView(bitalino.getBitalinoState());
    }

    public void start_recording() {
        if (!bitalino.start_recording(new int[]{CHANNEL}, SAMPLE_RATE)) {
            view.errMsg("Fehler beim Starten der Aufnahme!");
        }
        drawView(bitalino.getBitalinoState());

    }

    public void stop_reording() {
        if (!bitalino.stop_recording()) {
            view.errMsg("Fehler beim Stoppern er Aufnahmen");
        }
        drawView(bitalino.getBitalinoState());
    }

    public void disconnect_sensor() {
        bitalino.disconnect_sensor();

        view.unregisterReceiverView();
        view.unregisterReceiverCalcData();

        drawView(bitalino.getBitalinoState());
    }

    public void handleBroadcastViewState(Constants.States state) {
        switch (state) {
            case NO_CONNECTION:
                bitalino.setState(new BitalinoStateDisconnected());
                break;
            case LISTEN:
                bitalino.setState(new BitalinoStateConnected());
                break;
            case CONNECTING:
                bitalino.setState(new BitalinoStateConnected());
                break;
            case CONNECTED:
                bitalino.setState(new BitalinoStateConnected());
                break;
            case ACQUISITION_TRYING:
                bitalino.setState(new BitalinoStateRecording());
                break;
            case ACQUISITION_OK:
                bitalino.setState(new BitalinoStateRecording());
                break;
            case ACQUISITION_STOPPING:
                bitalino.setState(new BitalinoStateRecording());
                break;
            case DISCONNECTED:
                bitalino.setState(new BitalinoStateDisconnected());
                break;
            case ENDED:
                bitalino.setState(new BitalinoStateDisconnected());
                break;
        }
        drawView(bitalino.getBitalinoState());

    }

    public void handleBroadcastViewData(BITalinoFrame frame) {
        drawView(new SensorData("", frame.getAnalog(CHANNEL), System.currentTimeMillis()));
    }

    public void handleBroadcastCalcData(BITalinoFrame frame) {
        id++;
        if (frame.getAnalog(CHANNEL) > 0) {
            subscription().add(
                    repository.save(new SensorData(id + "", frame.getAnalog(CHANNEL), System.currentTimeMillis()))
                            .subscribeOn(threadingModule.getIOScheduler())
                            .observeOn(threadingModule.getMainScheduler())
                            .subscribe(new Observer<SensorData>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(SensorData data) {
                                }
                            })
            );
        }
    }

    private void drawView(IBitalinoState state) {
        view.drawState(state);
    }

    private void drawView(SensorData data) {
        view.drawData(data);
    }

    public void drawGraph() {
        Log.i(TAG, "##################################");
        subscription().add(
                repository.get()
                        .map(sensorData -> {
                            int sum = 0;
                            for (SensorData number : sensorData) {
                                sum = sum + number.data;
                            }
                            sum = sum / sensorData.size();

                            x++;
                            Log.i(TAG, "data: " + sum + " x " + x + " size" + sensorData.size());

                            return new DataPoint(x, sum);
                        })
                        .subscribeOn(threadingModule.getIOScheduler())
                        .observeOn(threadingModule.getMainScheduler())
                        .subscribe(new Observer<DataPoint>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(DataPoint dataPoint) {
                                view.addGraphData(dataPoint);
                            }
                        })
        );
    }
}
