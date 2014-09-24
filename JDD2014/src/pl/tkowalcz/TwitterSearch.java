package pl.tkowalcz;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import se.akerfeldt.signpost.retrofit.RetrofitHttpOAuthConsumer;
import se.akerfeldt.signpost.retrofit.SigningOkClient;

import java.util.List;

public class TwitterSearch {

	public static void main(String[] args) {
		RetrofitHttpOAuthConsumer oAuthConsumer = new RetrofitHttpOAuthConsumer("", "");
		oAuthConsumer.setTokenWithSecret("", "");

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint("https://api.twitter.com/1.1")
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setClient(new SigningOkClient(oAuthConsumer))
				.build();

		TwitterUserSearch service = restAdapter.create(TwitterUserSearch.class);
		List<String> reactive = service.searchUsers("Reactive");
		System.out.println(reactive);
	}
}

interface TwitterUserSearch {

	@GET("/users/search.json")
	List<String> searchUsers(@Query("q") String user);
}
