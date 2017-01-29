package workyfie.github.de.workyfie.data.repos.session;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import workyfie.github.de.workyfie.data.persistance.models.PersistanceSession;
import workyfie.github.de.workyfie.data.persistance.models.converter.SessionPersistanceViewConverter;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionPersistanceDataSource {

    private SessionPersistanceViewConverter converter;

    public SessionPersistanceDataSource() {
        converter = new SessionPersistanceViewConverter();
    }

    public Observable<Session> get(String id) {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            PersistanceSession persistance = realm.where(PersistanceSession.class)
                    .equalTo("id", Long.valueOf(id))
                    .findFirst();
            if (persistance != null) {
                subscriber.onNext(converter.from(persistance));
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
        if (realm.where(PersistanceSession.class).max("id") == null) {
            return "0";
        }
        return String.valueOf(realm.where(PersistanceSession.class).max("id").intValue() + 1);
    }

    public Observable<List<Session>> list() {
        return Observable.create(subscriber -> {
            Realm realm = Realm.getDefaultInstance();
            List<Session> sessionList = converter.from(
                    realm.copyFromRealm(
                            realm.where(PersistanceSession.class)
                                    .findAll()
                    )
            );

            if (sessionList.size() > 0) {
                subscriber.onNext(sessionList);
            }
            subscriber.onCompleted();
        });
    }
}
