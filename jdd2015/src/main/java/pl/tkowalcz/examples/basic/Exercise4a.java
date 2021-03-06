package pl.tkowalcz.examples.basic;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import pl.tkowalcz.twitter.RetroTwitter;
import rx.Observable;

public class Exercise4a {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault()) {
            httpClient.start();

            RetroTwitter twitter = new RetroTwitter();

            Observable.from(Arrays.asList("JDD", "GeeCON"));

            System.in.read();
        }
    }
}