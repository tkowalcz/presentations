package pl.tkowalcz;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class StreamingTwitterClientTest {

	@Test
	public void shouldGetUserStatuses() {
		BasicConfigurator.configure();
		// Given
		StreamingTwitterClient client = new StreamingTwitterClient();
		Gson gson = new GsonBuilder().create();

		// When
		client.tweets()
				.filter((string) -> string.contains("created_at"))
				.map((string) -> gson.fromJson(string, CreatedTweet.class))
				.filter(CreatedTweet::isValidTweet)
				.throttleFirst(1000, TimeUnit.MILLISECONDS)
				.subscribe(System.out::println, Throwable::printStackTrace);

		// Then
		Uninterruptibles.sleepUninterruptibly(1, TimeUnit.DAYS);
	}
}

class CreatedTweet {

	class User {
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

	@SerializedName("text")
	private String text;

	@SerializedName("user")
	private User user;

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
}