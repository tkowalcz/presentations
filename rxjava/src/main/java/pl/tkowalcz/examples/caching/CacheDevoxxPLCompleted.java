package pl.tkowalcz.examples.caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import pl.tkowalcz.examples.ui.TwitterUser;
import pl.tkowalcz.twitter.RetroTwitter;
import rx.Observable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CacheDevoxxPLCompleted {

    private static final int CACHE_EXPIRATION_MINUTES = 10;

    private final LoadingCache<String, Observable<List<TwitterUser>>> cache;

    public static void main(String[] args) throws IOException {
        new CacheDevoxxPLCompleted();
        System.in.read();
    }

    CacheDevoxxPLCompleted() {
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