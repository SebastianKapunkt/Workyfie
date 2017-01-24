package workyfie.github.de.workyfie.application.bitalino.reciever;

import android.util.Log;

import org.threeten.bp.Instant;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.state.IBitalinoState;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointRepository;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

import static workyfie.github.de.workyfie.presentation.page.main.sensor.SensorPresenter.TAG;

public class BitalinoReceiveHandler implements IBitalinoOnReceiveCallback {

    private BitalinoProxy bitalinoProxy;

    private String currentSessionId;
    private Instant oldInstant;

    private GraphDataPointRepository graphDataPointRepository;
    private SessionRepository sessionRepository;
    private ThreadingModule threadingModule;
    private CompositeSubscription subscription;

    private ArrayList<IBitalinoReceiverStateCallback> subscriberListState;
    private ArrayList<IBitalinoReceiverDataCallback> subscriberListData;

    public BitalinoReceiveHandler(BitalinoProxy bitalinoProxy,
                                  GraphDataPointRepository graphDataPointRepository,
                                  SessionRepository sessionRepository,
                                  ThreadingModule threadingModule) {

        subscriberListState = new ArrayList<>();
        subscriberListData = new ArrayList<>();
        subscription = new CompositeSubscription();

        this.bitalinoProxy = bitalinoProxy;
        this.graphDataPointRepository = graphDataPointRepository;
        this.sessionRepository = sessionRepository;
        this.threadingModule = threadingModule;

        currentSessionId = "";
    }

    public boolean subscribeState(IBitalinoReceiverStateCallback subscriber) {
        if (subscriberListState.contains(subscriber))
            return false;

        subscriberListState.add(subscriber);
        return true;
    }

    public boolean subscribeData(IBitalinoReceiverDataCallback subscriber) {
        if (subscriberListData.contains(subscriber))
            return false;

        subscriberListData.add(subscriber);
        return true;
    }

    public boolean unsubscribeState(IBitalinoReceiverStateCallback subscriber) {
        return subscriberListState.remove(subscriber);
    }

    public boolean unsubscribeData(IBitalinoReceiverDataCallback subscriber) {
        return subscriberListData.remove(subscriber);
    }

    public void setNewSession(String sessionId) {
        this.currentSessionId = sessionId;

        //clear old Data
        graphDataPointRepository.getFromTemp();
        oldInstant = null;
    }


    private void notifyAllStateChange(IBitalinoState state) {
        for (IBitalinoReceiverStateCallback subscriber : subscriberListState) {
            subscriber.callback(state);
        }
    }

    private void notityAllNewData(GraphDataPoint data) {
        for (IBitalinoReceiverDataCallback subscriber : subscriberListData) {
            subscriber.callback(data);
        }
    }

    @Override
    public void onReceiveData(SensorData sensorData) {
        if (currentSessionId == "") {
            try {
                throw new Exception("Session Id is not set");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        subscription.add(
                graphDataPointRepository.saveToTemp(new GraphDataPoint("", String.valueOf(currentSessionId), (double) 0, (double) sensorData.data))
                        .subscribeOn(threadingModule.getIOScheduler())
                        .observeOn(threadingModule.getMainScheduler())
                        .subscribe(new Observer<GraphDataPoint>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(GraphDataPoint dataPoint) {
                            }
                        })
        );

        //Check Time
        if (oldInstant == null) {
            oldInstant = Instant.now();
            return;
        }
        //zeitintervall 1 Sekunde
        if (Instant.now().getEpochSecond() - oldInstant.getEpochSecond() > 0.5) {
            oldInstant = Instant.now();


            sessionRepository.get(String.valueOf(currentSessionId))
                    .flatMap(session ->
                            Observable.zip(
                                    Observable.just((double) (Instant.now().getEpochSecond() - session.startTime.getEpochSecond())),
                                    graphDataPointRepository.getFromTemp().filter(data -> data.size() > 0),
                                    (diff, data) -> {
                                        Double sum = 0.0;

                                        for (GraphDataPoint point : data) {
                                            sum = sum + point.y;
                                        }

                                        return new GraphDataPoint(
                                                String.valueOf(System.currentTimeMillis()),
                                                Integer.valueOf(currentSessionId).toString(),
                                                diff,
                                                sum / data.size()
                                        );
                                    })
                    )
                    .flatMap(graphDataPointRepository::save)
                    .subscribeOn(threadingModule.getIOScheduler())
                    .observeOn(threadingModule.getMainScheduler())
                    .subscribe(new Observer<GraphDataPoint>() {
                        @Override
                        public void onCompleted() {
                            Log.i(TAG, "completed");
                        }

                        @Override
                        public void onError(Throwable e) {
//                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(GraphDataPoint graphDataPoint) {
                            Log.i(TAG, "new GrapDataPoint: "
                                    + graphDataPoint.id
                                    + " x: "
                                    + graphDataPoint.x
                                    + " y: "
                                    + graphDataPoint.y
                                    + " sessionId: "
                                    + graphDataPoint.sessionId
                            );
                            notityAllNewData(graphDataPoint);
                        }
                    });
        }
    }

    @Override
    public void onReceiveState(IBitalinoState state) {
        bitalinoProxy.setState(state);
        notifyAllStateChange(state);
    }
}
