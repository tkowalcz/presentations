package pl.tkowalcz.examples.streaming;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import pl.tkowalcz.twitter.Tweet;

import java.io.IOException;

public class StreamingJDD {

	public static void main(String[] args) throws IOException {
		StreamingTwitterClient client = new StreamingTwitterClient();
		Gson gson = new GsonBuilder().create();

		client.tweets()
				.filter((string) -> string.contains("created_at"))
				.map((string) -> gson.fromJson(string, Tweet.class))
				.filter(Tweet::isValidTweet)
				.subscribe(System.out::println, Throwable::printStackTrace);
	}
}
