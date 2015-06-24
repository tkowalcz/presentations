package pl.tkowalcz.examples.streaming;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import pl.tkowalcz.twitter.Tweet;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StreamingDevoxxPL {

    public static void main(String[] args) throws IOException {
        try (StreamingTwitterClient client = new StreamingTwitterClient()) {
            Gson gson = new GsonBuilder().create();

            Observable<GroupedObservable<String, String>> groupBy = client.tweets()
                    .filter(s -> s.contains("created_at"))
                    .map(s -> gson.fromJson(s, Tweet.class))
                    .retry()
                    .take(1000)
                    .skip(10)
                    .filter(Tweet::isValidTweet)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .map(Tweet::getText)
                    .flatMap(tweet -> {
                        String[] split = tweet.split(" ");
                        return Observable.from(split);
                    })
                    .groupBy(new Func1<String, String>() {
                        @Override
                        public String call(String word) {
                            return word;
                        }
                    });
            groupBy
                    .map(new Func1<GroupedObservable<String,String>, Object>() {
                        @Override
                        public Object call(GroupedObservable<String, String> wordStrem) {
                            return wordStrem.count();
                        }
                    })
                    .subscribe(System.out::println);


            System.in.read();
        }
    }
}
