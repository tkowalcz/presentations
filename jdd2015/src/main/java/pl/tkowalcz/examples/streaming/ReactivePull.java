package pl.tkowalcz.examples.streaming;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.util.concurrent.Uninterruptibles;
import com.lmax.disruptor.dsl.Disruptor;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class ReactivePull {

    public static final int RING_BUFFER_SIZE = 16;

    public static void main(String[] args) throws IOException {
        Disruptor<TweetEvent> disruptor = new Disruptor<>(TweetEvent::new,
                RING_BUFFER_SIZE, Executors.newSingleThreadExecutor());
        TweetsSubscriber subscriber = new TweetsSubscriber(disruptor);

        //noinspection unchecked
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            writeToSlowStorage(event.getTweet());
            subscriber.requestItems(1);
        });

        disruptor.start();

        Observable<String> martinOdersky = TwitterStream.ofUser("odersky");
        Subscription subscription = martinOdersky.subscribe(subscriber);

        System.in.read();
        subscription.unsubscribe();
    }

    private static void writeToSlowStorage(String tweet) {
        Uninterruptibles.sleepUninterruptibly(ThreadLocalRandom.current().nextInt(5), TimeUnit.SECONDS);
        LogHelper.log("[Consumer]", tweet);
    }

    private static class TweetsSubscriber extends Subscriber<String> {

        private final Disruptor<TweetEvent> disruptor;

        @Override
        public void onStart() {
            request(RING_BUFFER_SIZE);
        }

        @Override
        public void onCompleted() {
            disruptor.shutdown();
        }

        @Override
        public void onError(Throwable e) {
            disruptor.shutdown();
        }

        @Override
        public void onNext(String tweet) {
            disruptor.publishEvent((event, sequence) -> event.setTweet(tweet));
        }

        public TweetsSubscriber(Disruptor<TweetEvent> disruptor) {
            this.disruptor = disruptor;
        }

        public void requestItems(int request) {
            request(request);
        }
    }
}

class TweetEvent {

    private String tweet;

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}

class TwitterStream {

    public static Observable<String> ofUser(String username) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            private AtomicLong requestedItemsCount = new AtomicLong();

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.setProducer(requestedItemsCount::addAndGet);

                while (true) {
                    if (!subscriber.isUnsubscribed()) {
                        long requestedItemsLeft = requestedItemsCount.get();

                        if (requestedItemsLeft > 0) {
                            LogHelper.log("[Producer]", "Requested items left: %s, producing", requestedItemsLeft);
                            subscriber.onNext(username + ": scala is a gateway drug to haskell");
                            requestedItemsCount.decrementAndGet();
                        } else {
                            LogHelper.log("[Producer]", "No requested items left, dins something else");
                            Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
                            // Do useful things instead of blocking on that subscriber
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }
}

class LogHelper {

    public static void log(String actor, String message) {
        System.out.println(LocalTime.now() + " " + actor + " " + message);
    }

    public static void log(String actor, String format, Object... messages) {
        String message = String.format(format, messages);
        System.out.println(LocalTime.now() + " " + actor + " " + message);
    }
}
