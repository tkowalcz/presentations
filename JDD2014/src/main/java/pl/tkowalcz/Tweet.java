package pl.tkowalcz;

import com.google.gson.annotations.SerializedName;

public class Tweet {
	@SerializedName("created_at")
	private String DateCreated;
	@SerializedName("id")
	private String Id;
	@SerializedName("text")
	private String Text;
	@SerializedName("in_reply_to_status_id")
	private String InReplyToStatusId;
	@SerializedName("in_reply_to_user_id")
	private String InReplyToUserId;
	@SerializedName("in_reply_to_screen_name")
	private String InReplyToScreenName;
}
