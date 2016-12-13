package workyfie.github.de.workyfie.data.repository.interfaces;

import java.util.List;

public interface SimpleDataSource<T> {

    List<T> list();

    List<T> save(List<T> list);

    T get(String id);

    T save(T value);
}
