package workyfie.github.de.workyfie.data.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import rx.Observer;
import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointRepository;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPersistService extends IntentService {
    public static final String TAG = GraphDataPersistService.class.getSimpleName();
    public static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";

    public GraphDataPersistService() {
        super(GraphDataPersistService.class.getSimpleName());
    }

    public static Intent newInstance(Context context, String sessionId) {
        Intent intent = new Intent(context, GraphDataPersistService.class);
        intent.putExtra(EXTRA_SESSION_ID, sessionId);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String sessionId = intent.getStringExtra(EXTRA_SESSION_ID);

        GraphDataPointRepository repository = App.getComponent().getGraphDataPointRepository();

        repository
                .getFromTemp()
                .filter(data -> data.size() > 0)
                .map(data -> {
                    Double sum = 0.0;

                    for (GraphDataPoint point : data) {
                        sum = sum + point.y;
                    }

                    return new GraphDataPoint(
                            "",
                            sessionId,
                            data.get(data.size() - 1).x,
                            sum / data.size()
                    );
                })
                .flatMap(repository::save)
                .subscribeOn(App.getComponent().getThreadingModule().getIOScheduler())
                .subscribe(new Observer<GraphDataPoint>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
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
                    }
                });
    }
}
