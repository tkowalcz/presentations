package pl.tkowalcz.examples.basic;

import java.io.IOException;

import pl.tkowalcz.twitter.RetroTwitter;

public class Exercise5 {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        new Exercise5();
        System.in.read();
    }

    Exercise5() throws IOException {
        RetroTwitter mainNode = new RetroTwitter()
                .injectFailureWithProbability(0.9);

        RetroTwitter fallbackNode = new RetroTwitter()
                .injectFailureWithProbability(0.1);

        System.in.read();
    }
}
