package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import workyfie.github.de.workyfie.data.repos.interfaces.SimpleCacheDataSource;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointCacheDataSource extends SimpleCacheDataSource<GraphDataPoint> {

    @Override
    public String getId(GraphDataPoint value) {
        return value.id;
    }

    public Observable<List<GraphDataPoint>> getBySessionId(String sessionId) {
        return Observable.create(subscriber -> {
            List<GraphDataPoint> withSessionId = new ArrayList<>();

            for (GraphDataPoint graphDataPoint : map.values()) {
                if (graphDataPoint.sessionId.equals(sessionId)) {
                    withSessionId.add(graphDataPoint);
                }
            }
            if (withSessionId.size() > 0) {
                subscriber.onNext(withSessionId);
            }
            subscriber.onCompleted();
        });
    }
}
