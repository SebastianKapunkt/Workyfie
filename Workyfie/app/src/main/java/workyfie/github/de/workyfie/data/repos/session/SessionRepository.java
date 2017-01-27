package workyfie.github.de.workyfie.data.repos.session;

import android.util.Log;

import org.threeten.bp.Instant;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionRepository {
    public static final String TAG = SessionRepository.class.getSimpleName();

    private SessionPersistanceDataSource persistance;
    private SessionCacheDataSource cache;

    public SessionRepository(SessionPersistanceDataSource persistance, SessionCacheDataSource cache) {
        this.persistance = persistance;
        this.cache = cache;
    }

    public Observable<Session> get(String id) {
        return cache.get(id)
                .switchIfEmpty(
                        persistance.get(id)
                                .flatMap(cache::save)
                );
    }

    public Observable<Session> save(Session session) {
        return persistance.save(session)
                .flatMap(cache::save);
    }

    public Observable<List<Session>> list() {
        return cache.list()
                .switchIfEmpty(
                        persistance.list()
                                .flatMap(cache::save)
                );
    }
}
