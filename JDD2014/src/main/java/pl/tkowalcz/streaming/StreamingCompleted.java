package pl.tkowalcz.streaming;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.StreamingTwitterClient;
import rx.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StreamingCompleted {

	public static void main(String[] args) throws IOException {
		StreamingTwitterClient client = new StreamingTwitterClient();
		Gson gson = new GsonBuilder().create();

		Subscription subscription = client.tweets()
				.filter((string) -> string.contains("created_at"))
				.map((string) -> gson.fromJson(string, Tweet.class))
				.filter(Tweet::isValidTweet)
				.throttleFirst(1000, TimeUnit.MILLISECONDS)
				.retry()
				.subscribe(System.out::println, Throwable::printStackTrace);

		// Then
		System.in.read();

		subscription.unsubscribe();
		client.close();
		Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
	}
}
