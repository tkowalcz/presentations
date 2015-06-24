package pl.tkowalcz.examples.basic;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.Tweet;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.io.IOException;

public class BasicsCompleted {

    public static final Splitter SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();

    public static void main(String[] args) throws IOException {

        String tweet1 = "{\"created_at\": \"Fri Oct 10 11:26:33 +0000 2014\", \"text\": \"Charging by induction is like proof by induction. It takes ages and you're never quite sure if it worked.\", \"user\": { \"screen_name\": \"tastapod\", \"location\": \"Agilia\" }}";
        String tweet2 = "{\"created_at\": \"Fri Oct 10 11:26:33 +0000 2014\", \"text\": \"TIL the movie Inception was based on node.js callbacks\", \"user\": { \"screen_name\": \"HackerNewsOnion\", \"location\": \"HackerNews\" }}";
        String tweet3 = "{\"created_at\": \"Fri Oct 10 12:26:33 +0000 2014\", \"text\": \"Shark Facts: Statistically, you are a more likely to be killed by a statistician than by a shark\", \"user\": { \"screen_name\": \"TheTechShark\"}}";
        String tweet4 = "{\"delete\":{\"status\":{\"id\":48223552775705600}}}";
        String tweet5 = "{\"created_at\": \"Fri Oct 10 08:26:33 +0000 2014\", \"text\": \"Keep calm and don't block threads\", \"user\": { \"screen_name\": \"tkowalcz\", \"location\": \"DevoxxPL\"}}";

        // Will deserialize tweets from json
        Gson gson = new GsonBuilder().create();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("Thread.currentThread() = " + Thread.currentThread());
                ImmutableList<String> tweets = ImmutableList.of(tweet1, tweet2, tweet3, tweet4, tweet5);
                for (String tweet : tweets) {
                    if (subscriber.isUnsubscribed()) {
                        break;
                    }
                    subscriber.onNext(tweet);
                }
                subscriber.onCompleted();
            }
        })
                .observeOn(Schedulers.computation())
                        // We are only interested in new tweets so lets filter out others
                .filter(s -> s.contains("created_at"))
                        // Deserialize from JSON
                .map(s -> gson.fromJson(s, Tweet.class))
                        // Pass only tweets that are 'valid' - contain nonempty text, location and author
                .filter(Tweet::isValidTweet)
                        // Lets roll
                .subscribe(tweet -> {
                    System.out.println("Thread.currentThread() = " + Thread.currentThread());
                }, System.out::println);
    }

}
