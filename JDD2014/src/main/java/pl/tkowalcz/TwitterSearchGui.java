package pl.tkowalcz;

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
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.log4j.BasicConfigurator;
import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.schedulers.JavaFxScheduler;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import static rx.javafx.sources.ObservableValueSource.fromObservableValue;
import static rx.observables.JavaFxObservable.fromNodeEvents;

public class TwitterSearchGui extends Application {

	private final ITwitterSearch twitter = new CachingTwitterSearch(new RetroTwitter());
	private final CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

	private TextArea textField;
	private ListView<TwitterUserWithImage> listView;

	public static void main(String[] args) {
		BasicConfigurator.configure();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		httpClient.start();

		BorderPane border = new BorderPane();
		border.setPadding(new Insets(10, 25, 10, 25));

		Scene scene = new Scene(border, 500, 225);

		Label remainingChars = new Label("140");

		textField = new TextArea();
		textField.setWrapText(true);

		fromObservableValue(textField.textProperty())
				.map((text) -> 140 - text.length())
				.doOnNext((remaining) -> remainingChars.setText(Integer.toString(remaining)))
				.map((remaining) -> {
					if (remaining < 0) {
						return Color.RED;
					} else if (remaining < 20) {
						return Color.YELLOW;
					}

					return Color.BLACK;
				})
				.subscribe(remainingChars::setTextFill);

		HBox content = new HBox();

		listView = new ListView<>();
		listView.setCellFactory(param -> new TwitterUserCell());
		content.getChildren().add(listView);

		PopupControl control = new PopupControl();
		control.getScene().setRoot(content);

		Observable<KeyEvent> keyPressesFromWindow = fromNodeEvents(border, KeyEvent.KEY_PRESSED);
		Observable<KeyEvent> keyPressesFromList = fromNodeEvents(listView, KeyEvent.KEY_PRESSED);
		Observable.merge(keyPressesFromWindow, keyPressesFromList)
				.filter((KeyEvent keyEvent) -> keyEvent.getCode() == KeyCode.ESCAPE)
				.subscribe((ignored) -> {
					control.hide();
					listView.getItems().clear();
				});

		HBox bottom = new HBox();
		Observable<Number> caretPositionObservable = fromObservableValue(textField.caretPositionProperty());
		caretPositionObservable
				.map(Number::intValue)
				.filter((position) -> position > 0)
				.map((position) -> getWordAtIndex(textField.getText(), position))
				.distinctUntilChanged()
				.filter((word) -> word.startsWith("@") && word.length() > 3)
				.throttleLast(250, TimeUnit.MILLISECONDS)
				.flatMap((word) -> twitter.searchUsers(word).takeUntil(caretPositionObservable.skip(1)))
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

					users.forEach(
							(user) ->
									ObservableHttp
											.createGet(user.getTwitterUser().getProfileImageUrl(), httpClient)
											.toObservable()
											.flatMap(ObservableHttpResponse::getContent)
											.map(ByteArrayInputStream::new)
											.map(Image::new)
											.observeOn(JavaFxScheduler.getInstance())
											.subscribe((image) -> {
												int currentIndex = listView.getItems().indexOf(user);
												TwitterUserWithImage element = new TwitterUserWithImage(user.getTwitterUser());
												element.setImage(image);

												listView.getItems().set(currentIndex, element);
											}, Throwable::printStackTrace)
					);

				}, Throwable::printStackTrace);

		border.setCenter(textField);

		Button tweet = new Button("Tweet");
		tweet.setDefaultButton(true);
		tweet.setOnAction((event) -> {
			if (!textField.getText().isEmpty()) {
				twitter.tweet(textField.getText());
			}
		});

		bottom.setPadding(new Insets(10, 0, 0, 0));
		bottom.setSpacing(10);
		bottom.setAlignment(Pos.BASELINE_RIGHT);
		bottom.getChildren().addAll(remainingChars, tweet);
		border.setBottom(bottom);

		primaryStage.setTitle("Compose new Tweet");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	static String getWordAtIndex(String text, Integer position) {
		int start = position;
		for (; start > 0; start--) {
			if (Character.isWhitespace(text.charAt(start - 1))) {
				break;
			}
		}

		if (start == position) {
			return StringUtils.EMPTY;
		}

		return text.substring(start, position);
	}
}
