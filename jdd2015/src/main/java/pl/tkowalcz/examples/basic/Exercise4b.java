package pl.tkowalcz.examples.basic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import pl.tkowalcz.asciigen.ASCII;
import pl.tkowalcz.twitter.RetroTwitter;
import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;

public class Exercise4b {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault()) {
            httpClient.start();

            RetroTwitter twitter = new RetroTwitter();

            ASCII ascii = new ASCII();
            Observable.from(Arrays.asList("Devoxx", "GeeCON", "JDD"))
                    .flatMap(twitter::searchUsers)
                    .flatMap(Observable::from)
                    .flatMap(user -> ObservableHttp
                            .createGet(user.getProfileImageUrl(), httpClient)
                            .toObservable())
                    .flatMap(ObservableHttpResponse::getContent)
                    .map(bytes -> {
                        try {
                            return ImageIO.read(new ByteArrayInputStream(bytes));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    })
                    .zipWith(Observable.timer(5, 5, TimeUnit.SECONDS), (image, ignore) -> image)
                    .map(ascii::convert)
                    .subscribe((x) -> {
                        System.out.println(x);
                        System.out.println();
                    });

            System.in.read();
        }
    }
}