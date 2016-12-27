package workyfie.github.de.workyfie.data.repos.sensordata;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import workyfie.github.de.workyfie.data.models.SensorData;

public class SensorDataRepository {
    public static final String TAG = SensorDataRepository.class.getSimpleName();

    private SensorDataCacheDataSource cache;

    public SensorDataRepository(SensorDataCacheDataSource cache) {
        this.cache = cache;
    }

    public Observable<SensorData> get(String id) {
        return cache.get(id);
    }

    public Observable<List<SensorData>> get() {
        return cache.list()
                .doOnNext(ingored -> cache.clearCache());
    }

    public Observable<SensorData> save(SensorData data) {
        return Observable.just(data)
                .filter(data1 -> data.data > 0 && data.data < 1000)
                .flatMap(cache::save);
    }
}
