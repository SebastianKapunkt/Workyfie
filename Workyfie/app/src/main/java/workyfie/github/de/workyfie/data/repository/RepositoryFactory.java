package workyfie.github.de.workyfie.data.repository;

import workyfie.github.de.workyfie.data.repository.sensordata.SensorDataCacheDataSource;
import workyfie.github.de.workyfie.data.repository.sensordata.SensorDataRepository;

/**
 * Singleton Pattern
 */
public class RepositoryFactory {
    private static RepositoryFactory instance;

    private SensorDataRepository sensorDataRepository;

    private RepositoryFactory(){
        sensorDataRepository = new SensorDataRepository(new SensorDataCacheDataSource());
    }

    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }

    public SensorDataRepository getSensorDataRepository() {
        return sensorDataRepository;
    }
}
