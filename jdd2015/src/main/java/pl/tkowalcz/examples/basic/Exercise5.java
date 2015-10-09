package pl.tkowalcz.examples.basic;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import pl.tkowalcz.twitter.RetroTwitter;
import pl.tkowalcz.twitter.TwitterUser;
import rx.Observable;

public class Exercise5 {

    private static final int CACHE_EXPIRATION_MINUTES = 10;

    private final LoadingCache<String, Observable<List<TwitterUser>>> cache;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        new Exercise5();
        System.in.read();
    }

    Exercise5() {
        RetroTwitter twitter = new RetroTwitter();

        Function<String, Observable<List<TwitterUser>>> searcher = new Function<String, Observable<List<TwitterUser>>>() {

            @Override
            public Observable<List<TwitterUser>> apply(String prefix) {
                return twitter.searchUsers(prefix)
                        .doOnError(throwable -> cache.invalidate(prefix))
                        .cache();
            }
        };

        cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
                .build(CacheLoader.from(searcher::apply));

        Observable<List<TwitterUser>> devoxx = cache.getUnchecked("Devoxx");
        devoxx.subscribe(System.out::println);
    }
}