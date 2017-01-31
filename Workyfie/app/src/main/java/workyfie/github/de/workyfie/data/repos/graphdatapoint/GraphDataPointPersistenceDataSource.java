package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import workyfie.github.de.workyfie.data.persistence.models.PersistenceGraphDataPoint;
import workyfie.github.de.workyfie.data.persistence.models.PersistenceSession;
import workyfie.github.de.workyfie.data.persistence.models.converter.GraphDataPointPersistenceViewConverter;
import workyfie.github.de.workyfie.data.view.models.GraphDataPoint;

public class GraphDataPointPersistenceDataSource {

    private GraphDataPointPersistenceViewConverter converter;

    public GraphDataPointPersistenceDataSource() {
        converter = new GraphDataPointPersistenceViewConverter();
    }

    public Observable<List<GraphDataPoint>> getBySessionId(String sessionId) {
        return Observable.create(subscriber -> {
            List<GraphDataPoint> points = new ArrayList<>();

            Realm realm = Realm.getDefaultInstance();

            points = converter.from(
                    realm.copyFromRealm(
                            realm.where(PersistenceGraphDataPoint.class)
                                    .equalTo("sessionId", sessionId)
                                    .findAll()
                    )
            );

            if (points.size() > 0) {
                subscriber.onNext(points);
            }
            subscriber.onCompleted();
        });
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
        if (realm.where(PersistenceGraphDataPoint.class).max("id") == null) {
            return "0";
        }
        return String.valueOf(realm.where(PersistenceGraphDataPoint.class).max("id").intValue() + 1);
    }

    public Observable<String> deleteById(String id) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
            RealmResults<PersistenceGraphDataPoint> rows = realm.where(PersistenceGraphDataPoint.class).equalTo("sessionId", id).findAll();
            rows.deleteAllFromRealm();
            realm.commitTransaction();

            subscriber.onNext(id);
            subscriber.onCompleted();
        });
    }
}
