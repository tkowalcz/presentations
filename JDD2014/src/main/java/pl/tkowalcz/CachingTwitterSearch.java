package pl.tkowalcz;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rx.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CachingTwitterSearch implements ITwitterSearch {

	private static final int CACHE_EXPIRATION_MINUTES = 10;

	private final LoadingCache<String, Observable<List<TwitterUser>>> cache;

	public CachingTwitterSearch(ITwitterSearch twitter) {
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

	}

	@Override
	public Observable<List<TwitterUser>> searchUsers(String prefix) {
		return cache.getUnchecked(prefix);
	}
}
