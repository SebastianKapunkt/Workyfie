package workyfie.github.de.workyfie.data.repos.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

public abstract class SimpleCacheDataSource<T> implements SimpleDataSource<T> {
    public static final String TAG = SimpleCacheDataSource.class.getSimpleName();
    protected Map<String, T> map = new HashMap<>();

    @Override
    public Observable<List<T>> list() {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                if (!map.isEmpty()) {
                    subscriber.onNext(new ArrayList<>(map.values()));
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<T>> save(final List<T> valuesList) {
        return Observable.defer(new Func0<Observable<List<T>>>() {
            @Override
            public Observable<List<T>> call() {
                for (T values : valuesList) {
                    map.put("" + getId(values), values);
                }
                return Observable.just(valuesList);
            }
        });
    }

    @Override
    public Observable<T> get(final String id) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (map.containsKey(id)) {
                    subscriber.onNext(map.get(id));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<T> save(final T value) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                String key = "" + getId(value);
                if(map.containsKey(key)) {
                    map.remove(key);
                }
                map.put(key, value);
                subscriber.onNext(value);
                subscriber.onCompleted();
            }
        });
    }

    public abstract String getId(T value);

    public void clearCache() {
        map.clear();
    }
}
