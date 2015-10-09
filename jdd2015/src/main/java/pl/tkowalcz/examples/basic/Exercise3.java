package pl.tkowalcz.examples.basic;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.tuple.Pair;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import pl.tkowalcz.twitter.Tweet;
import rx.Observable;

public class Exercise3 {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (StreamingTwitterClient client = new StreamingTwitterClient()) {
            Gson gson = new GsonBuilder().create();

            Observable<String> twitterObservable = client.tweets();

            twitterObservable
                    .filter(string -> string.contains("created_at"))
                    .map(string -> gson.fromJson(string, Tweet.class))
                    .map(Tweet::getText)
                    .flatMap(text -> Observable.from(text.split(" ")))
                    .filter(word -> word.length() <= 3)
                    .take(1000)
                    .doOnNext(word -> System.out.println("Received new word = " + word))
                    .groupBy(word -> word)
                    .flatMap(byWord -> byWord
                            .count()
                            .map(word -> Pair.of(byWord.getKey(), word)))
                    .toSortedList((pair1, pair2) -> pair1.getValue().compareTo(pair2.getValue()))
                    .flatMap(Observable::from)
                    .subscribe(pair -> System.out.println("pair = " + pair));

            System.in.read();
        }
    }
}
