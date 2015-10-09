package pl.tkowalcz.twitter;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.ning.http.client.*;
import com.ning.http.client.oauth.ConsumerKey;
import com.ning.http.client.oauth.OAuthSignatureCalculator;
import com.ning.http.client.oauth.RequestToken;
import rx.Observable;
import rx.Subscriber;

public class StreamingTwitterClient implements AutoCloseable {

    private final AsyncHttpClient httpClient = new AsyncHttpClient();

    public Observable<String> tweets() {
        OAuthSignatureCalculator calculator = getOAuthSignatureCalculator();

        return Observable.create((Subscriber<? super String> subscriber) -> {
            try {
                httpClient
                        .prepareGet("https://stream.twitter.com/1.1/statuses/sample.json")
                        .setSignatureCalculator(calculator)
                        .execute(new StreamingObservableHandler(subscriber));
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    private OAuthSignatureCalculator getOAuthSignatureCalculator() {
        ConsumerKey consumerKey = new ConsumerKey("UXTi7xA1mxrQawuhUVRAcBsmF", "ZXAJRSEnCc6bansHVlcVHtfjnQACcdzJ6VPudroEUufGcePCtm");
        RequestToken requestToken = new RequestToken("1295001146-W7oX12GXjQ4Ef2kRZlVvJMEf6HoP4oqak4jrc81", "7gmtXuXYavfjMvjuwnVQ71dNuFGc1dZk3hWyGSTaMDMcH");
        return new OAuthSignatureCalculator(consumerKey, requestToken);
    }

    @Override
    public void close() {
        httpClient.close();
    }

    private static class StreamingObservableHandler implements AsyncHandler<Object> {

        private final Subscriber<? super String> subscriber;
        private StringBuffer builder;

        public StreamingObservableHandler(Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
            builder = new StringBuffer();
        }

        @Override
        public void onThrowable(Throwable t) {
            subscriber.onError(t);
        }

        @Override
        public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) {
            ByteBuffer cursor = bodyPart.getBodyByteBuffer();

            if (subscriber.isUnsubscribed()) {
                return STATE.ABORT;
            }

            while (cursor.remaining() > 0) {
                byte character = cursor.get();
                if (character == '\n') {
                    subscriber.onNext(builder.toString());
                    builder = new StringBuffer();
                } else {
                    builder.append((char) character);
                }
            }

            return STATE.CONTINUE;
        }

        @Override
        public STATE onStatusReceived(HttpResponseStatus responseStatus) {
            return STATE.CONTINUE;
        }

        @Override
        public STATE onHeadersReceived(HttpResponseHeaders headers) {
            return STATE.CONTINUE;
        }

        @Override
        public Object onCompleted() {
            if (subscriber.isUnsubscribed()) {
                return null;
            }

            if (builder.length() != 0) {
                subscriber.onNext(builder.toString());
            }

            subscriber.onCompleted();
            return null;
        }
    }
}
