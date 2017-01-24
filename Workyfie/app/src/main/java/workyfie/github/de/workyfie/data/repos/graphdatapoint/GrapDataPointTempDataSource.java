package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GrapDataPointTempDataSource {

    private Map<String, GraphDataPoint> map = new HashMap<>();
    private int tempId = 0;

    public Observable<GraphDataPoint> save(GraphDataPoint graphDataPoint) {
        return Observable.create(subscriber -> {
            tempId ++;

            GraphDataPoint dataPoint = GraphDataPoint.setId(graphDataPoint, String.valueOf(tempId));

            if (map.containsKey(dataPoint.id)) {
                map.remove(dataPoint.id);
            }
            map.put(dataPoint.id, graphDataPoint);
            
            subscriber.onNext(graphDataPoint);
            subscriber.onCompleted();
        });
    }

    public Observable<List<GraphDataPoint>> getAndClear() {
        return Observable.create(new Observable.OnSubscribe<List<GraphDataPoint>>() {
            @Override
            public void call(Subscriber<? super List<GraphDataPoint>> subscriber) {
                if (!map.isEmpty()) {
                    tempId = 0;
                    subscriber.onNext(new ArrayList<>(map.values()));
                }

                subscriber.onCompleted();
            }
        })
                .doOnNext(ignored -> map.clear());
    }
}
