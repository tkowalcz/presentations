package pl.tkowalcz.twitter;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

public class Tweet {

	@SerializedName("text")
	private String text;

	@SerializedName("user")
	private User user = new User();

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getScreenName() {
		return user.getScreenName();
	}

	public String getLocation() {
		return user.getLocation();
	}

	public boolean isValidTweet() {
		return StringUtils.isNoneEmpty(text, getScreenName(), getLocation());
	}

	@Override
	public String toString() {
		return String.format("%s in %s said: %s", getScreenName(), getLocation(), text);
	}

	static class User {

		@SerializedName("screen_name")
		private String screenName;

		@SerializedName("location")
		private String location;

		public String getScreenName() {
			return screenName;
		}

		public void setScreenName(String screenName) {
			this.screenName = screenName;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}
	}
}