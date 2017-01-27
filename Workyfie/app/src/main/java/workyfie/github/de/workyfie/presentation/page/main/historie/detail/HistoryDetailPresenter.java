package workyfie.github.de.workyfie.presentation.page.main.historie.detail;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointRepository;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;
import workyfie.github.de.workyfie.data.view.models.common.Tuple2;
import workyfie.github.de.workyfie.presentation.mvp.Presenter;

public class HistoryDetailPresenter implements
        Presenter<HistoryDetailView>,
        Observer<Tuple2<Session, List<GraphDataPoint>>> {
    public static final String TAG = HistoryDetailPresenter.class.getSimpleName();

    private final ThreadingModule threadingModule;
    private GraphDataPointRepository graphRepository;
    private SessionRepository sessionRepository;
    private final String sessionId;

    private CompositeSubscription subscription;
    private HistoryDetailView view;

    public HistoryDetailPresenter(ThreadingModule threadingModule, GraphDataPointRepository graphDataPointRepository, SessionRepository sessionRepository, String id) {
        this.threadingModule = threadingModule;
        this.graphRepository = graphDataPointRepository;
        this.sessionRepository = sessionRepository;
        sessionId = id;
    }

    @Override
    public void attach(HistoryDetailView view) {
        Log.v(TAG, "attach");
        this.view = view;
    }

    @Override
    public void detach() {
        Log.v(TAG, "detach");
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

    private void drawView(Tuple2<Session, List<GraphDataPoint>> items) {
        view.drawContent(items);
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted called");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(Tuple2<Session, List<GraphDataPoint>> items) {
        drawView(items);
    }

    void requestContent() {
        subscription().add(
                Observable.zip(
                        sessionRepository.get(sessionId)
                                .switchIfEmpty(Observable.just(
                                        new Session(sessionId, sessionId + " TEST DATA", Instant.now(), Instant.now())
                                )),
                        graphRepository.getBySessionId(sessionId)
                                .switchIfEmpty(Observable.create(subscriber -> {

                                    List<GraphDataPoint> points = new ArrayList<>();
                                    for (int i = 0; i < 100; i++) {
                                        Random rnd = new Random();
                                        points.add(new GraphDataPoint(i + "", sessionId, (double) i, 0 + (rnd.nextDouble() * (1000))));
                                    }

                                    subscriber.onNext(points);
                                    subscriber.onCompleted();
                                }))
                                .map(graphDataPoints -> {
                                    Collections.sort(graphDataPoints, new GraphDataPointComparator());
                                    return graphDataPoints;
                                }),
                        Tuple2::new
                )
                        .subscribeOn(threadingModule.getIOScheduler())
                        .observeOn(threadingModule.getMainScheduler())
                        .subscribe(this)
        );
    }
}
