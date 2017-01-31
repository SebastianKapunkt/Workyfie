package workyfie.github.de.workyfie.data.repos.session;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionRepository {
    public static final String TAG = SessionRepository.class.getSimpleName();

    private SessionPersistenceDataSource persistence;
    private SessionCacheDataSource cache;

    public SessionRepository(SessionPersistenceDataSource persistence, SessionCacheDataSource cache) {
        this.persistence = persistence;
        this.cache = cache;
    }

    public Observable<Session> get(String id) {
        return persistence.get(id);
    }

    public Observable<Session> save(Session session) {
        return persistence.save(session)
                .flatMap(cache::save);
    }

    public Observable<List<Session>> list() {
        return persistence.list()
                .flatMap(cache::save)
                .map(sessions -> {
                    Collections.reverse(sessions);
                    return sessions;
                });
    }

    public Observable<String> delete(String id) {
        return persistence.delete(id)
                .flatMap(string -> App.getComponent().getGraphDataPointRepository().deleteBySessionId(id))
                .doOnNext(ignore -> cache.clearCache());
    }
}
