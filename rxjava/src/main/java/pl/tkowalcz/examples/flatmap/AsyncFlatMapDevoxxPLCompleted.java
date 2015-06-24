package pl.tkowalcz.examples.flatmap;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import pl.tkowalcz.twitter.RetroTwitter;
import rx.Observable;
import rx.apache.http.ObservableHttp;

import java.io.IOException;
import java.util.Arrays;

public class AsyncFlatMapDevoxxPLCompleted {

    public static void main(String[] args) throws IOException {
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault()) {
            httpClient.start();

            RetroTwitter twitter = new RetroTwitter();

            Observable.from(Arrays.asList("Devoxx", "GeeCON"))
                    .flatMap(twitter::searchUsers)
                    .flatMap(Observable::from)
                    .flatMap(user -> ObservableHttp
                            .createGet(user.getProfileImageUrl(), httpClient)
                            .toObservable())
                    .subscribe(System.out::println);

            System.in.read();
        }
    }
}