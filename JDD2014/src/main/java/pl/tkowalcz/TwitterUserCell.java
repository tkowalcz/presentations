package pl.tkowalcz;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TwitterUserCell extends ListCell<TwitterUserWithImage> {

	private final HBox hbox = new HBox();
	private final Label label = new Label();
	private final ImageView imageView = new ImageView();

	public TwitterUserCell() {
		hbox.getChildren().addAll(imageView, label);
	}

	@Override
	protected void updateItem(TwitterUserWithImage item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null) {
			label.setText(item.getTwitterUser().getName());

			if (item.getImage() != null) {
				imageView.setImage(item.getImage());
			}

			setGraphic(hbox);
		}
	}
}
