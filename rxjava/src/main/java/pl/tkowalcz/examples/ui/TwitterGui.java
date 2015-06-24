package pl.tkowalcz.examples.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import pl.tkowalcz.twitter.ITwitterSearch;
import pl.tkowalcz.twitter.RetroTwitter;
import pl.tkowalcz.utils.WordUtils;
import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.schedulers.JavaFxScheduler;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import static pl.tkowalcz.utils.FxToRx.observe;
import static pl.tkowalcz.utils.FxToRx.observeKeyPress;

public class TwitterGui extends Application {

    private final ITwitterSearch twitter = new CachingTwitterSearch(new RetroTwitter());
    private final CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

    private TextArea textField;
    private ListView<TwitterUserWithImage> listView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10, 25, 10, 25));

        HBox content = new HBox();
        HBox bottom = new HBox();

        Scene scene = new Scene(mainPane, 500, 225);

        textField = new TextArea();
        textField.setWrapText(true);
        textField.setFont(Font.font(24));
        mainPane.setCenter(textField);

        listView = new ListView<>();
        listView.setCellFactory(TwitterUserCell::new);
        content.getChildren().add(listView);

        Label remainingChars = new Label("140");
        Button tweet = new Button("Tweet");
        tweet.setDefaultButton(true);

        bottom.setPadding(new Insets(10, 0, 0, 0));
        bottom.setSpacing(10);
        bottom.setAlignment(Pos.BASELINE_RIGHT);
        bottom.getChildren().addAll(remainingChars, tweet);
        mainPane.setBottom(bottom);

        PopupControl control = new PopupControl();
        control.getScene().setRoot(content);

        primaryStage.setTitle("Compose new Tweet");
        primaryStage.setScene(scene);
        primaryStage.show();

        httpClient.start();

        observe(textField.textProperty())
                .map((text) -> 140 - text.length())
                .doOnNext((remaining) -> remainingChars.setText(remaining.toString()))
                .map((remaining) -> {
                    if (remaining <= 0) {
                        return Color.RED;
                    } else if (remaining < 20) {
                        return Color.YELLOW;
                    }

                    return Color.BLACK;
                })
                .subscribe(remainingChars::setTextFill);

        Observable<KeyEvent> keyPressesFromWindow = observeKeyPress(mainPane);
        Observable<KeyEvent> keyPressesFromList = observeKeyPress(listView);
        Observable.merge(keyPressesFromWindow, keyPressesFromList)
                .filter((KeyEvent keyEvent) -> keyEvent.getCode() == KeyCode.ESCAPE)
                .subscribe((ignored) -> {
                    listView.getItems().clear();
                    control.hide();
                });

        Observable<Number> caretPositionObservable = observe(textField.caretPositionProperty());
        caretPositionObservable
                .map(Number::intValue)
                .filter((position) -> position > 0)
                .map((position) -> WordUtils.getWordAtIndex(textField.getText(), position))
                .distinctUntilChanged()
                .filter((word) -> word.startsWith("@") && word.length() > 3)
                .throttleLast(250, TimeUnit.MILLISECONDS)
                .flatMap((word) -> twitter.searchUsers(word).takeUntil(caretPositionObservable))
                .flatMap((listOfUsers) ->
                                Observable.from(listOfUsers).map(TwitterUserWithImage::new).toList()
                )
                .filter((list) -> !list.isEmpty())
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe((users) -> {
                    Point2D point2D = bottom.localToScreen(0, 10);
                    control.show(bottom, point2D.getX(), point2D.getY());

                    listView.getItems().clear();
                    listView.getItems().addAll(users);

                    Observable.from(users)
                            .takeUntil(caretPositionObservable)
                            .subscribe(
                                    (user) ->
                                            ObservableHttp
                                                    .createGet(user.getTwitterUser().getProfileImageUrl(), httpClient)
                                                    .toObservable()
                                                    .flatMap(ObservableHttpResponse::getContent)
                                                    .map(ByteArrayInputStream::new)
                                                    .map(Image::new)
                                                    .observeOn(JavaFxScheduler.getInstance())
                                                    .takeUntil(caretPositionObservable)
                                                    .subscribe((image) -> {
                                                        int currentIndex = listView.getItems().indexOf(user);
                                                        if (currentIndex >= 0) {
                                                            TwitterUserWithImage element = new TwitterUserWithImage(user.getTwitterUser());
                                                            element.setImage(image);

                                                            listView.getItems().set(currentIndex, element);
                                                        }
                                                    }, Throwable::printStackTrace));
                }, Throwable::printStackTrace);
    }
}
