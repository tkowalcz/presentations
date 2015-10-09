package pl.tkowalcz.examples.basic;

import java.io.IOException;

import org.testng.annotations.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.ReplaySubject;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertNull;

public class Exercise7 {

    @Test
    public void shouldCacheAndReplay() {
        // Given
        IObservablesCache<String, String> cache = null;

        ReplaySubject<String> subject = ReplaySubject.create();
        subject.onNext("Foo");
        subject.onNext("Bar");
        subject.onNext("FooBar");
        subject.onCompleted();

        // When
//        Observable<String> key = cache.get("Key");
        Observable<String> key = subject.asObservable();

        // Then
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        key.subscribe(subscriber);

        subscriber.assertReceivedOnNext(asList("Foo", "Bar", "FooBar"));
    }

    @Test
    public void shouldNotCacheObservableWithError() {
        // Given
        IObservablesCache<String, String> cache = null;

        ReplaySubject<String> subject = ReplaySubject.create();
        subject.onNext("Foo");
        subject.onError(new IOException("Broken pipe"));

        // When
        Observable<String> key = cache.get("Key");

        // Then
        assertNull(key);
    }
}
