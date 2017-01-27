package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.List;

import rx.Observable;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointRepository {

    private GraphDataPointPersistanceDataSource persistance;
    private GraphDataPointCacheDataSource cache;
    private GrapDataPointTempDataSource temp;

    public GraphDataPointRepository(GraphDataPointPersistanceDataSource persistance, GraphDataPointCacheDataSource cache, GrapDataPointTempDataSource temp) {
        this.persistance = persistance;
        this.cache = cache;
        this.temp = temp;
    }

    public Observable<List<GraphDataPoint>> getBySessionId(String sessionId) {
        return cache.getBySessionId(sessionId)
                .switchIfEmpty(
                        persistance.getBySessionId(sessionId)
                                .flatMap(cache::save)
                );
    }

    public Observable<GraphDataPoint> saveToTemp(GraphDataPoint graphDataPoint) {
        return Observable.just(graphDataPoint)
                .filter(point -> point.y > 0 && point.y < 1000)
                .flatMap(temp::save);
    }

    public Observable<List<GraphDataPoint>> getFromTemp() {
        return temp.getAndClear();
    }

    public Observable<GraphDataPoint> save(GraphDataPoint graphDataPoint) {
        return persistance.save(graphDataPoint)
                .flatMap(cache::save);
    }
}
