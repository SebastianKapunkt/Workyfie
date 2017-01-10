package workyfie.github.de.workyfie.data.repos.session;

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

            Session value = Session.setId(session, getNextKey(realm));

            realm.beginTransaction();
            realm.copyToRealm(converter.to(value));
            realm.commitTransaction();

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
}
