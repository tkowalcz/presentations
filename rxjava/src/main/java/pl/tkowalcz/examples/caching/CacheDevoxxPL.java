package pl.tkowalcz.examples.caching;

import com.google.common.cache.LoadingCache;
import pl.tkowalcz.examples.ui.TwitterUser;
import pl.tkowalcz.twitter.RetroTwitter;
import rx.Observable;

import java.io.IOException;
import java.util.List;

public class CacheDevoxxPL {

    private static final int CACHE_EXPIRATION_MINUTES = 10;

    private final LoadingCache<String, Observable<List<TwitterUser>>> cache;

    public static void main(String[] args) throws IOException {
        new CacheDevoxxPL();
        System.in.read();
    }

    CacheDevoxxPL() {
        RetroTwitter twitter = new RetroTwitter();
        cache = null;
    }
}