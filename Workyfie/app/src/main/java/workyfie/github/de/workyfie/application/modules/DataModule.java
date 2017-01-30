package workyfie.github.de.workyfie.application.modules;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.bitalino.reciever.BitalinoReceiveHandler;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointCacheDataSource;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointPersistenceDataSource;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointRepository;
import workyfie.github.de.workyfie.data.repos.session.SessionCacheDataSource;
import workyfie.github.de.workyfie.data.repos.session.SessionPersistenceDataSource;
import workyfie.github.de.workyfie.data.repos.session.SessionRepository;

/**
 * provides the modules for the app
 */
public class DataModule {
    public static final String TAG = DataModule.class.getSimpleName();

    private App app;

    public DataModule(App app) {
        this.app = app;
    }

    public BitalinoProxy provideBitalinoProxy() {
        return new BitalinoProxy();
    }

    public BitalinoReceiveHandler provideBitalinoReceiveDataHandler() {
        return new BitalinoReceiveHandler(App.getComponent().getBitalinoProxy(),
                App.getComponent().getGraphDataPointRepository(),
                App.getComponent().getSessionRepository(),
                App.getComponent().getThreadingModule());
    }

    public SessionRepository provideSessionRepository() {
        return new SessionRepository(
                new SessionPersistenceDataSource(),
                new SessionCacheDataSource()
        );
    }

    public GraphDataPointRepository provideGrapDataPointRepository() {
        return new GraphDataPointRepository(
                new GraphDataPointPersistenceDataSource(),
                new GraphDataPointCacheDataSource()
        );
    }
}
