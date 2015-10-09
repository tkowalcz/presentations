package pl.tkowalcz.examples.basic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.imageio.ImageIO;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import pl.tkowalcz.asciigen.ASCII;
import pl.tkowalcz.twitter.RetroTwitter;

public class Exercise4b {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault()) {
            httpClient.start();

            RetroTwitter twitter = new RetroTwitter();

            ASCII ascii = new ASCII();
            byte[] bytes = new byte[0];
            try {
                BufferedImage image = ImageIO.read(new File("jdd2015/src/main/resources/rx.png"));
                System.out.println(ascii.convert(image));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            System.in.read();
        }
    }
}