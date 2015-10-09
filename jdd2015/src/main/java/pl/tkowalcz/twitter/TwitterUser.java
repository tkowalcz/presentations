package pl.tkowalcz.twitter;

import com.google.gson.annotations.SerializedName;

public class TwitterUser {

	@SerializedName("name")
	private String name;

	@SerializedName("profile_image_url")
	private String profileImageUrl;

	public TwitterUser() {
	}

	public TwitterUser(String name, String profileImageUrl) {
		this.name = name;
		this.profileImageUrl = profileImageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}
