package workyfie.github.de.workyfie.data.repos.session;

import rx.Observable;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionRepository {

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
}
