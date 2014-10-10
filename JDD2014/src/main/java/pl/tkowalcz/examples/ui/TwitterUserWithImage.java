package pl.tkowalcz.examples.ui;

import javafx.scene.image.Image;

public class TwitterUserWithImage {

	private final TwitterUser twitterUser;
	private Image image;

	public TwitterUserWithImage(TwitterUser twitterUser) {
		this.twitterUser = twitterUser;
	}

	public TwitterUser getTwitterUser() {
		return twitterUser;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
