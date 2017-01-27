package workyfie.github.de.workyfie.data.repos.graphdatapoint;

import java.util.ArrayList;
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
        return Observable.create(subscriber -> {
            List<GraphDataPoint> points = new ArrayList<>();

            Realm realm = Realm.getDefaultInstance();

            points = converter.from(
                    realm.copyFromRealm(
                            realm.where(PersistanceGraphDataPoint.class)
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
        if (realm.where(PersistanceGraphDataPoint.class).max("id") == null) {
            return "0";
        }
        return String.valueOf(realm.where(PersistanceGraphDataPoint.class).max("id").intValue() + 1);
    }
}
