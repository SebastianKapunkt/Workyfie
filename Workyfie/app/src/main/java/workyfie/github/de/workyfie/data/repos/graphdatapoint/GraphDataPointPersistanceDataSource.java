package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceGraphDataPoint;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceSession;
import workyfie.github.de.workyfie.data.persistance.models.converter.GraphDataPointPersistanceViewConverter;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;
import workyfie.github.de.workyfie.data.view.models.Session;

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

    public Observable<GraphDataPoint> save(final GraphDataPoint graphDataPoint) {
        return Observable.create(subscriber -> {
            GraphDataPoint local = graphDataPoint;

            Realm realm = Realm.getDefaultInstance();

            if (graphDataPoint.id.isEmpty()) {
                local = GraphDataPoint.setId(graphDataPoint, getNextKey(realm));
            }

            try {
                realm.beginTransaction();
                realm.copyToRealm(converter.to(local));
                realm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }

            subscriber.onNext(local);

            subscriber.onCompleted();
        });
    }

    public String getNextKey(Realm realm) {
        if (realm.where(PersistanceGraphDataPoint.class).max("id") == null) {
            return "0";
        }
        return String.valueOf(realm.where(PersistanceGraphDataPoint.class).max("id").intValue() + 1);
    }
}
