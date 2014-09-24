package pl.tkowalcz;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class NameService {

	private final LoadingCache<String, Observable<String>> cache;

	public NameService(Function<String, Observable<String>> remoteService, int expirationTimeMinutes) {
		Function<String, Observable<String>> pathsLoader = new Function<String, Observable<String>>() {

			@Override
			public Observable<String> apply(String input) {
				return remoteService.apply(input)
						.doOnError(throwable -> cache.invalidate(input))
						.cache();
			}
		};

		cache = CacheBuilder.newBuilder().expireAfterWrite(expirationTimeMinutes, TimeUnit.MINUTES).build(CacheLoader.from(pathsLoader));
	}

	public Observable<String> getNames(String prefix) {
		return cache.getUnchecked(prefix);
	}
}
