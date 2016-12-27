package workyfie.github.de.workyfie.application;

import workyfie.github.de.workyfie.App;
import workyfie.github.de.workyfie.application.bitalino.BitalinoProxy;
import workyfie.github.de.workyfie.application.modules.DataModule;
import workyfie.github.de.workyfie.application.modules.ThreadingModule;
import workyfie.github.de.workyfie.data.repos.sensordata.SensorDataRepository;

/**
 * hold the modules for the app
 */
public class ApplicationComponent {

    private DataModule dataModule;
    private ThreadingModule threadingModule;

    private SensorDataRepository sensorDataRepository;
    private BitalinoProxy bitalinoProxy;

    public ApplicationComponent(App app) {
        threadingModule = new ThreadingModule();
        dataModule = new DataModule(app);
    }

    public DataModule getDataModule() {
        return dataModule;
    }

    public ThreadingModule getThreadingModule() {
        return threadingModule;
    }

    public SensorDataRepository getSensorDataRepository() {
        if (sensorDataRepository == null) {
            sensorDataRepository = dataModule.provideSensorDataRepository();
        }
        return sensorDataRepository;
    }

    public BitalinoProxy getBitalinoProxy() {
        if (bitalinoProxy == null) {
            bitalinoProxy = dataModule.provideBitalinoProxy();
        }
        return bitalinoProxy;
    }
}
