package workyfie.github.de.workyfie.application.modules;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GrapDataPointTempDataSource;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointCacheDataSource;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointPersistanceDataSource;
import workyfie.github.de.workyfie.data.repos.graphdatapoint.GraphDataPointRepository;
import workyfie.github.de.workyfie.data.repos.sensordata.SensorDataCacheDataSource;
import workyfie.github.de.workyfie.data.repos.sensordata.SensorDataRepository;
import workyfie.github.de.workyfie.data.repos.session.SessionCacheDataSource;
import workyfie.github.de.workyfie.data.repos.session.SessionPersistanceDataSource;
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

    public SensorDataRepository provideSensorDataRepository() {
        return new SensorDataRepository(
                new SensorDataCacheDataSource()
        );
    }

    public BitalinoProxy provideBitalinoProxy() {
        return new BitalinoProxy();
    }

    public SessionRepository provideSessionRepository() {
        return new SessionRepository(
                new SessionPersistanceDataSource(),
                new SessionCacheDataSource()
        );
    }

    public GraphDataPointRepository provideGrapDataPointRepository() {
        return new GraphDataPointRepository(
                new GraphDataPointPersistanceDataSource(),
                new GraphDataPointCacheDataSource(),
                new GrapDataPointTempDataSource());
    }
}
