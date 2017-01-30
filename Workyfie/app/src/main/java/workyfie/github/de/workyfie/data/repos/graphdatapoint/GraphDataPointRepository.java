package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.presentation.page.main.historie.detail.GraphDataPointComparator;

public class GraphDataPointRepository {

    private GraphDataPointPersistenceDataSource persistence;
    private GraphDataPointCacheDataSource cache;

    public GraphDataPointRepository(GraphDataPointPersistenceDataSource persistence, GraphDataPointCacheDataSource cache) {
        this.persistence = persistence;
        this.cache = cache;
    }

    public Observable<List<GraphDataPoint>> getBySessionId(String sessionId) {
        return cache.getBySessionId(sessionId)
                .switchIfEmpty(
                        persistence.getBySessionId(sessionId)
                                .flatMap(cache::save)
                ).map(graphDataPoints -> {
                    Collections.sort(graphDataPoints, new GraphDataPointComparator());
                    return graphDataPoints;
                });
    }

    public Observable<GraphDataPoint> save(GraphDataPoint graphDataPoint) {
        return persistence.save(graphDataPoint)
                .flatMap(cache::save);
    }
}
