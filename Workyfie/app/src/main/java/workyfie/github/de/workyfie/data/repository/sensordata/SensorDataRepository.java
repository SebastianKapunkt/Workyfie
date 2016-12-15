package workyfie.github.de.workyfie.data.repository.sensordata;

import java.util.List;

import workyfie.github.de.workyfie.data.models.SensorData;

public class SensorDataRepository {
    public static final String TAG = SensorDataRepository.class.getSimpleName();

    private SensorDataCacheDataSource cache;

    public SensorDataRepository(SensorDataCacheDataSource cache) {
        this.cache = cache;
    }

    public SensorData get(String id) {
        return cache.get(id);
    }

    public void add(SensorData data){ cache.save(data); }

    public List<SensorData> get() {
        return cache.list();
    }
}
