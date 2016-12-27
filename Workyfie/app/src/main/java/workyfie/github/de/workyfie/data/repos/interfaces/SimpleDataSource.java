package workyfie.github.de.workyfie.data.repos.interfaces;

import java.util.List;

import rx.Observable;

public interface SimpleDataSource<T> {

    Observable<List<T>> list();

    Observable<List<T>> save(List<T> list);

    Observable<T> get(String id);

    Observable<T> save(T value);
}
