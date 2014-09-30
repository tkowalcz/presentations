package pl.tkowalcz;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import twitter4j.User;

public class TwitterUserCell extends ListCell<User> {

	private final HBox hbox = new HBox();
	private final Label label = new Label("");
	private final ImageView imageView = new ImageView();

	public TwitterUserCell() {
		hbox.getChildren().addAll(imageView, label);
	}

	@Override
	protected void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		setText(null);
		if (item != null) {
			label.setText(item.getName());
//			imageView.setImage(new Image(item.getProfileImageURL()));
			setGraphic(hbox);
		}
	}
}
