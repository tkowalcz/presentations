package pl.tkowalcz.twitter;

import pl.tkowalcz.examples.ui.TwitterUser;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

import java.util.List;

public interface RetroTwitterApi {

	@Headers({"Content-Type: application/json"})
	@GET("/1.1/users/search.json")
	Observable<List<TwitterUser>> searchForUsers(
			@Query("q") String prefix
	);
}
