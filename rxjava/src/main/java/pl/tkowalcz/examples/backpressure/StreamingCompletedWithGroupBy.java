package pl.tkowalcz.examples.backpressure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.tuple.Pair;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import pl.tkowalcz.twitter.Tweet;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

import java.io.IOException;

public class StreamingCompletedWithGroupBy {

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
                    .flatMap(new Func1<GroupedObservable<String, String>, Observable<Pair<String, Integer>>>() {
                        @Override
                        public Observable<Pair<String, Integer>> call(GroupedObservable<String, String> byWord) {
                            return byWord
                                    .count()
                                    .map(word -> Pair.of(byWord.getKey(), word));
                        }
                    })
                    .toSortedList((pair1, pair2) -> pair1.getValue().compareTo(pair2.getValue()))
                    .flatMap(Observable::from)
                    .subscribe(pair -> System.out.println("pair = " + pair));

            System.in.read();
        }
    }
}
