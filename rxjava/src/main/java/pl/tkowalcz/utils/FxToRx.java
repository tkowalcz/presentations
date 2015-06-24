package pl.tkowalcz.utils;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import rx.Observable;
import rx.observables.JavaFxObservable;

import static rx.javafx.sources.ObservableValueSource.fromObservableValue;

/**
 * This is a utility class that adapts JavaFx observable collections and event listeners to
 * be compatible with Rx interface. It is a thin wrapper over JavaFX adapter downloaded from github.
 * There are two reasons for creating it:
 * 1. I prefer shorter method names used here
 * 2. When wrapping a property using JavaFx adapter it will always emit an element representing
 * 	current value of that property, even if it did not change. I do not want that.
 */
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
}
