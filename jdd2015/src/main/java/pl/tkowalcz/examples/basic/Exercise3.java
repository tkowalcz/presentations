package pl.tkowalcz.examples.basic;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.StreamingTwitterClient;
import rx.Observable;

public class Exercise3 {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (StreamingTwitterClient client = new StreamingTwitterClient()) {
            Gson gson = new GsonBuilder().create();

            Observable<String> twitterObservable = client.tweets();

            System.in.read();
        }
    }
}
