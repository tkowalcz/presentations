package pl.tkowalcz.examples.streaming;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import pl.tkowalcz.twitter.Tweet;
import rx.Subscription;

public class StreamingCompleted {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (StreamingTwitterClient client = new StreamingTwitterClient()) {
            Gson gson = new GsonBuilder().create();

            Subscription subscription = client.tweets()
                    .filter(string -> string.contains("created_at"))
                    .map(string -> gson.fromJson(string, Tweet.class))
                    .filter(Tweet::isValidTweet)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
//                    .flatMap(tweet -> Observable.create(subscriber -> {
//                        String escape = UrlEscapers.urlPathSegmentEscaper().escape(tweet.getText());
//                        try {
//                            URL url = new URL("http://ws.detectlanguage.com/0.2/detect?q=" + escape + "&key=aa26afeebfc15628c9cb81b63932d279");
//                            String response = IOUtils.toString(url);
//                            subscriber.onNext(response);
//                            subscriber.onCompleted();
//                        } catch (IOException e) {
//                            subscriber.onError(e);
//                        }
//        }))
                    .retry()
                    .subscribe(System.out::println, Throwable::printStackTrace);

            System.in.read();

            subscription.unsubscribe();
        }
    }
}
