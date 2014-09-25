package pl.tkowalcz;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rx.Observable;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class NameService {

	private final LoadingCache<String, Observable<String>> cache;

	public NameService(java.util.function.Function<String, Observable<String>> remoteService, int expirationTimeMinutes) {
		Function<String, Observable<String>> loader = new Function<String, Observable<String>>() {

			@Override
			public Observable<String> apply(String input) {
				return remoteService.apply(input)
						.doOnError(throwable -> cache.invalidate(input))
						.cache();
			}
		};

		cache = CacheBuilder
				.newBuilder()
				.expireAfterWrite(expirationTimeMinutes, TimeUnit.MINUTES)
				.build(CacheLoader.from(loader::apply));
	}

	public Observable<String> getNames(String prefix) {
		return cache.getUnchecked(prefix);
	}
}
