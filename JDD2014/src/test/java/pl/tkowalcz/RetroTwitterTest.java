package pl.tkowalcz;

import org.testng.annotations.Test;

public class RetroTwitterTest {

	@Test
	public void shouldOAuth() {
		// Given
		RetroTwitter retroTwitter = new RetroTwitter();

		// When
		String token = retroTwitter.requestToken();

		// Then
		System.out.println("token = " + token);
	}
}