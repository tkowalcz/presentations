package pl.tkowalcz.examples.basic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.Tweet;
import rx.Observable;

import java.util.Arrays;

public class BasicsCompleted {

	public static void main(String[] args) {

		String tweet1 = "{\"created_at\": \"Fri Oct 10 11:26:33 +0000 2014\", \"text\": \"Charging by induction is like proof by induction. It takes ages and you're never quite sure if it worked.\", \"user\": { \"screen_name\": \tastapod\", \"location\": \"Agilia\" }}";
		String tweet2 = "{\"created_at\": \"Fri Oct 10 11:26:33 +0000 2014\", \"text\": \"TIL the movie Inception was based on node.js callbacks\", \"user\": { \"screen_name\": \"HackerNewsOnion\", \"location\": \"HackerNews\" }}";
		String tweet3 = "{\"created_at\": \"Fri Oct 10 12:26:33 +0000 2014\", \"text\": \"Shark Facts: Statistically, you are a more likely to be killed by a statistician than by a shark\", \"user\": { \"screen_name\": \"TheTechShark\"}}";
		String tweet4 = "{\"delete\":{\"status\":{\"id\":48223552775705600}}}";
		String tweet5 = "{\"created_at\": \"Fri Oct 10 08:26:33 +0000 2014\", \"text\": \"Keep calm and don't block threads\", \"user\": { \"screen_name\": \"tkowalcz\", \"location\": \"JDD Conference\"}}";

		// Will deserialize tweets from json
		Gson gson = new GsonBuilder().create();

		Observable
				// Creating observable that will emit elements of Iterable<String> (list of tweets) upon subscription;
				.from(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5))
						// We are only interested in new tweets so lets filter out others
				.filter(s -> s.contains("created_at"))
						// Deserialize from JSON
				.map(s -> gson.fromJson(s, Tweet.class))
						// Pass only tweets that are 'valid' - contain nonempty text, location and author
				.filter(Tweet::isValidTweet)
						// Lets roll
				.subscribe(System.out::println, System.out::println);
	}

}
