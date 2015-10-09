package pl.tkowalcz.examples.basic;

import rx.Observable;

public interface IObservablesCache<K, V> {

    Observable<V> get(K key);
}
