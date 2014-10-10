package pl.tkowalcz.examples.streaming;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.tkowalcz.twitter.StreamingTwitterClient;

import java.io.IOException;

public class StreamingJDD {

	public static void main(String[] args) throws IOException {
		StreamingTwitterClient client = new StreamingTwitterClient();
		Gson gson = new GsonBuilder().create();

	}
}
