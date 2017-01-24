package workyfie.github.de.workyfie.presentation.page.main.sensor;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import org.threeten.bp.Instant;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateConnected;
import workyfie.github.de.workyfie.application.bitalino.state.BitalinoStateRecording;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class SensorPresenter implements Presenter<SensorView> {
    public static final String TAG = SensorPresenter.class.getSimpleName();

    private final String MAC_ADRESS = "B0:B4:48:F0:CE:CA";
    private final int CHANNEL = 0; //A1 --> 0 A2 --> 1 "B0:B4:48:F0:C6:8A"
    private final int SAMPLE_RATE = 1000;

    private SensorView view;
    private BitalinoProxy bitalino;
    private BitalinoReceiveHandler bitalinoReceiveHandler;

    private CompositeSubscription subscription;
    private final ThreadingModule threadingModule;
    private SessionRepository sessionRepository;

    public SensorPresenter(ThreadingModule threadingModule, BitalinoProxy bitalino, SessionRepository sessionRepository, BitalinoReceiveHandler bitalinoReceiveHandler) {
        this.threadingModule = threadingModule;
        this.bitalino = bitalino;
        this.sessionRepository = sessionRepository;
        this.bitalinoReceiveHandler = bitalinoReceiveHandler;
    }

    @Override
    public void attach(SensorView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private CompositeSubscription subscription() {
        if (subscription == null) {
            subscription = new CompositeSubscription();
        }
        return subscription;
    }

    public boolean isSensorConnected() {
        return (bitalino.getBitalinoState() instanceof BitalinoStateConnected || bitalino.getBitalinoState() instanceof BitalinoStateRecording);
    }

    public void requestContent() {
        drawView(bitalino.getBitalinoState());
        //TODO check session noch aktiv wenn ja dann weiter zeichnen und aktuelle Session aus DB holen
    }

    public void connect_sensor(Context context) {
        bitalino.initBitalinoCommunicationBLE(context);
        if (!bitalino.connect_sensor(MAC_ADRESS)) {
            view.errMsg("Fehler beim Verbinden mit dem Sensor. Ist BT eingeschaltet?");
        }
    }

    public void start_recording() {
        subscription().add(
                sessionRepository.save(new Session("", "new_session", Instant.now(), null))
                        .subscribeOn(threadingModule.getIOScheduler())
                        .observeOn(threadingModule.getMainScheduler())
                        .subscribe(new Observer<Session>() {
                                       @Override
                                       public void onCompleted() {
                                           Log.i(TAG, "requestContent onCompleted");
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           e.printStackTrace();
                                       }

                                       @Override
                                       public void onNext(Session session) {
                                           Log.i(TAG, "id " + session.id);

                                           bitalinoReceiveHandler.setNewSession(session.id);

                                           if (!bitalino.start_recording(new int[]{CHANNEL}, SAMPLE_RATE)) {
                                               view.errMsg("Fehler beim Starten der Aufnahme!");
                                           }
                                       }
                                   }
                        ));

    }

    public void stop_reording(Context context) {
        //BUG SENSOR beim Recording nicht gestoppt werden, daher erst disconnecten und dann wieder connecten.
        bitalino.disconnect_sensor();
        new android.os.Handler().postDelayed(
                () -> connect_sensor(context),
                300);
    }

    public void disconnect_sensor() {
        bitalino.disconnect_sensor();
    }

    private void drawView(IBitalinoState state) {
        view.drawState(state);
    }

    private void drawView(GraphDataPoint data) {
        //view.drawDataList(data);
        view.addGraphData(new DataPoint(data.x, data.y));
    }


    public void handleBroadcastViewState(IBitalinoState state) {
        drawView(state);
    }

    public void handleBroadcastViewData(GraphDataPoint data) {
        drawView(data);
    }
}
