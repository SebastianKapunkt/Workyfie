package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.List;

import rx.Observable;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointRepository {

    private GraphDataPointPersistanceDataSource persistance;
    private GraphDataPointCacheDataSource cache;

    public GraphDataPointRepository(GraphDataPointPersistanceDataSource persistance, GraphDataPointCacheDataSource cache) {
        this.persistance = persistance;
        this.cache = cache;
    }

    public Observable<List<GraphDataPoint>> getBySessionId(String sessionId) {
        return cache.getBySessionId(sessionId)
                .switchIfEmpty(
                        persistance.getBySessionId(sessionId)
                        .flatMap(cache::save)
                );
    }
}
