package pl.tkowalcz.twitter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.TwitterUser;
import pl.tkowalcz.twitter.ITwitterSearch;
import pl.tkowalcz.twitter.RetroTwitterApi;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import se.akerfeldt.signpost.retrofit.RetrofitHttpOAuthConsumer;
import se.akerfeldt.signpost.retrofit.SigningOkClient;

import java.util.List;

public class RetroTwitter implements ITwitterSearch {

	private final RetroTwitterApi twitterService;

	public RetroTwitter() {
		Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

		RetrofitHttpOAuthConsumer oAuthConsumer = new RetrofitHttpOAuthConsumer("UXTi7xA1mxrQawuhUVRAcBsmF", "ZXAJRSEnCc6bansHVlcVHtfjnQACcdzJ6VPudroEUufGcePCtm");
		oAuthConsumer.setTokenWithSecret("1295001146-W7oX12GXjQ4Ef2kRZlVvJMEf6HoP4oqak4jrc81", "7gmtXuXYavfjMvjuwnVQ71dNuFGc1dZk3hWyGSTaMDMcH");

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setEndpoint("https://api.twitter.com").setConverter(new GsonConverter(gson))
				.setClient(new SigningOkClient(oAuthConsumer))
				.build();

		twitterService = restAdapter.create(RetroTwitterApi.class);
	}

	@Override
	public Observable<List<TwitterUser>> searchUsers(String prefix) {
		if (prefix.startsWith("@")) {
			prefix = prefix.substring(1);
		}

		if (prefix.isEmpty()) {
			return Observable.empty();
		}

		return twitterService.searchForUsers(prefix);
	}
}

