package workyfie.github.de.workyfie.data.repos.session;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import workyfie.github.de.workyfie.data.persistence.models.PersistenceSession;
import workyfie.github.de.workyfie.data.persistence.models.converter.SessionPersistenceViewConverter;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionPersistenceDataSource {

    private SessionPersistenceViewConverter converter;

    public SessionPersistenceDataSource() {
        converter = new SessionPersistenceViewConverter();
    }

    public Observable<Session> get(String id) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            PersistenceSession persistence = realm.where(PersistenceSession.class)
                    .equalTo("id", Long.valueOf(id))
                    .findFirst();
            if (persistence != null) {
                subscriber.onNext(converter.from(persistence));
            }
            subscriber.onCompleted();
        });
    }

    public Observable<Session> save(Session session) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            Session value;

            if (session.id.isEmpty()) {
                value = Session.setId(session, getNextKey(realm));
            } else {
                value = session;
            }

            try {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(converter.to(value));
                realm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }

            subscriber.onNext(value);
            subscriber.onCompleted();
        });
    }

    public String getNextKey(Realm realm) {
        if (realm.where(PersistenceSession.class).max("id") == null) {
            return "0";
        }
        return String.valueOf(realm.where(PersistenceSession.class).max("id").intValue() + 1);
    }

    public Observable<List<Session>> list() {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            List<Session> sessionList = converter.from(
                    realm.copyFromRealm(
                            realm.where(PersistenceSession.class)
                                    .findAll()
                    )
            );

            if (sessionList.size() > 0) {
                subscriber.onNext(sessionList);
            }
            subscriber.onCompleted();
        });
    }

    public Observable<String> delete(String id) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
            RealmResults<PersistenceSession> rows = realm.where(PersistenceSession.class).equalTo("id",Long.valueOf(id)).findAll();
            rows.deleteAllFromRealm();
            realm.commitTransaction();

            subscriber.onNext(id);
            subscriber.onCompleted();
        });
    }
}
