package workyfie.github.de.workyfie.data.repository.sensordata;

import workyfie.github.de.workyfie.data.models.SensorData;
import workyfie.github.de.workyfie.data.repository.interfaces.SimpleCacheDataSource;

public class SensorDataCacheDataSource extends SimpleCacheDataSource<SensorData> {
    @Override
    public String getId(SensorData value) {
        return value.id;
    }
}