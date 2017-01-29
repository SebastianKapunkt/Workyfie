package workyfie.github.de.workyfie.presentation.page.main.measure;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import org.threeten.bp.Instant;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.config.BitalinoConfig;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class MeasurePresenter implements Presenter<MeasureView> {
    private static final String TAG = MeasurePresenter.class.getSimpleName();

    private MeasureView view;

    private BitalinoProxy bitalino;
    private BitalinoReceiveHandler bitalinoReceiveHandler;
    private BitalinoConfig bitalinoConfig;

    private CompositeSubscription subscription;
    private final ThreadingModule threadingModule;
    private SessionRepository sessionRepository;

    private String sessionId;

    public MeasurePresenter(ThreadingModule threadingModule,
                            BitalinoProxy bitalino,
                            SessionRepository sessionRepository,
                            BitalinoReceiveHandler bitalinoReceiveHandler,
                            BitalinoConfig bitalinoConfig) {
        this.threadingModule = threadingModule;
        this.bitalino = bitalino;
        this.sessionRepository = sessionRepository;
        this.bitalinoReceiveHandler = bitalinoReceiveHandler;
        this.bitalinoConfig = bitalinoConfig;
    }

    @Override
    public void attach(MeasureView view) {
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

    public void requestContent() {
        drawView(bitalino.getBitalinoState());
    }

    private CompositeSubscription subscription() {
        if (subscription == null) {
            subscription = new CompositeSubscription();
        }
        return subscription;
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
                                           sessionId = session.id;
                                           bitalinoReceiveHandler.setNewSession(session.id);
                                           view.setGraphData(new DataPoint[]{});

                                           if (!bitalino.start_recording(new int[]{bitalinoConfig.CHANNEL}, bitalinoConfig.SAMPLE_RATE)) {
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
                1000);
        subscription().add(
                sessionRepository.get(sessionId)
                        .map(session -> Session.setEndTime(session, Instant.now()))
                        .flatMap(sessionRepository::save)
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
                                       }
                                   }
                        ));
    }

    private void connect_sensor(Context context) {
        bitalino.initBitalinoCommunicationBLE(context);
        if (!bitalino.connect_sensor()) {
            view.errMsg("Fehler beim Verbinden mit dem Sensor.");
        }
    }

    private void drawView(GraphDataPoint data) {
        view.addGraphData(new DataPoint(data.x, data.y));
    }

    private void drawView(IBitalinoState state) {
        view.drawState(state);
    }

    public void handleBroadcastViewState(IBitalinoState state) {
        drawView(state);
    }

    public void handleBroadcastViewData(GraphDataPoint data) {
        drawView(data);
    }
}
