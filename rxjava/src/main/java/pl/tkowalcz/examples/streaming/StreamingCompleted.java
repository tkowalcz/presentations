package pl.tkowalcz.examples.streaming;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import pl.tkowalcz.twitter.Tweet;
import rx.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StreamingCompleted {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (StreamingTwitterClient client = new StreamingTwitterClient()) {
            Gson gson = new GsonBuilder().create();

            Subscription subscription = client.tweets()
                    .filter((string) -> string.contains("created_at"))
                    .map((string) -> gson.fromJson(string, Tweet.class))
                    .filter(Tweet::isValidTweet)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .retry()
                    .subscribe(System.out::println, Throwable::printStackTrace);

            System.in.read();

            subscription.unsubscribe();
        }
    }
}
