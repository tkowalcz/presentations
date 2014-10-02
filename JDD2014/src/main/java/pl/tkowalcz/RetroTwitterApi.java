package pl.tkowalcz;

import retrofit.http.*;
import rx.Observable;

import java.util.List;

public interface RetroTwitterApi {
	@Headers({"Content-Type: application/json"})
	@GET("/1.1/users/search.json")
	Observable<List<TwitterUser>> searchForUsers(
			@Query("q") String prefix
	);

	@Headers({"Content-Type: application/json"})
	@POST("/oauth/request_token")
	String requestToken(@Header("oauth_callback") String callbackUrl);

	/**
	 * "Updates the authenticating user’s current status, also known as tweeting."
	 */
	@Headers({"Content-Type: application/json"})
	@POST("/1.1/statuses/update.json")
	Object updateStatus(
			@Header("Authorization")
			@Query("status") String content);
	//First status update from reactive twitter client
}
