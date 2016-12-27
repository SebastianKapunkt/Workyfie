package workyfie.github.de.workyfie.application.modules;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.data.repos.sensordata.SensorDataCacheDataSource;
import workyfie.github.de.workyfie.data.repos.sensordata.SensorDataRepository;

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
}
