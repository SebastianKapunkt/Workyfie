package workyfie.github.de.workyfie.data.repos.session;

import workyfie.github.de.workyfie.data.repos.interfaces.SimpleCacheDataSource;
import workyfie.github.de.workyfie.data.view.models.Session;

public class SessionCacheDataSource extends SimpleCacheDataSource<Session> {
    @Override
    public String getId(Session value) {
        return value.id;
    }
}
