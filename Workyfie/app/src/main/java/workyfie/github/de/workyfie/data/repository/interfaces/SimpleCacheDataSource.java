package workyfie.github.de.workyfie.data.repository.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class SimpleCacheDataSource<T> implements SimpleDataSource<T> {
    public static final String TAG = SimpleCacheDataSource.class.getSimpleName();
    protected Map<String, T> map = new HashMap<>();

    @Override
    public List<T> list() {
        if (map.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(map.values());
        }
    }

    @Override
    public List<T> save(final List<T> valuesList) {
        for (T values : valuesList) {
            map.put("" + getId(values), values);
        }
        return list();
    }

    @Override
    public T get(final String id) {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public T save(final T value) {
        String key = getId(value);

        if (map.containsKey(key)) {
            map.remove(key);
        }
        map.put(key, value);

        return map.get(key);
    }

    public abstract String getId(T value);

    public void clear() {
        map.clear();
    }
}
