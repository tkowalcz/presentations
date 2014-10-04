package pl.tkowalcz.utils;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;

import static rx.javafx.sources.ObservableValueSource.fromObservableValue;

public class FxToRx {

	public static <T> Observable<T> observe(ObservableValue<T> fxObservable) {
		return fromObservableValue(fxObservable).skip(1);
	}

	public static <T extends Event> Observable<T> observe(Node node, EventType<T> eventType) {
		return JavaFxObservable.fromNodeEvents(node, eventType);
	}

	public static Observable<KeyEvent> observeKeyPress(Node node) {
		return JavaFxObservable.fromNodeEvents(node, KeyEvent.KEY_PRESSED);
	}

	public static JavaFxScheduler guiThread() {
		return JavaFxScheduler.getInstance();
	}
}
