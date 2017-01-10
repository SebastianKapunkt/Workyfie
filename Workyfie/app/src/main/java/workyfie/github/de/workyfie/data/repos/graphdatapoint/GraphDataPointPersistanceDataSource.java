package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceGraphDataPoint;
import workyfie.github.de.workyfie.data.persistance.models.converter.GraphDataPointPersistanceViewConverter;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointPersistanceDataSource {

    private GraphDataPointPersistanceViewConverter converter;

    public GraphDataPointPersistanceDataSource() {
        converter = new GraphDataPointPersistanceViewConverter();
    }

    public Observable<List<GraphDataPoint>> getBySessionId(String sessionId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(PersistanceGraphDataPoint.class)
                .equalTo("sessionId", sessionId)
                .findAll()
                .asObservable()
                .map(persistance -> converter.from(realm.copyFromRealm(persistance)));
    }
}
