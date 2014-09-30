package pl.tkowalcz;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TwitterSearchGuiTest {

	@Test
	public void shouldReturnWord() {
		// Given
		String text = "Blah 123 Foobar";

		// When
		String actual1 = TwitterSearchGui.getWordAtIndex(text, 8);
		String actual2 = TwitterSearchGui.getWordAtIndex(text, 7);
		String actual3 = TwitterSearchGui.getWordAtIndex(text, 6);
		String actual4 = TwitterSearchGui.getWordAtIndex(text, 5);
		String actual5 = TwitterSearchGui.getWordAtIndex(text, 4);
		String actual6 = TwitterSearchGui.getWordAtIndex(text, 3);
		String actual7 = TwitterSearchGui.getWordAtIndex(text, 2);
		String actual8 = TwitterSearchGui.getWordAtIndex(text, 1);
		String actual9 = TwitterSearchGui.getWordAtIndex(text, 0);

		String actual10 = TwitterSearchGui.getWordAtIndex(text, 9);
		String actual11 = TwitterSearchGui.getWordAtIndex(text, 14);
		String actual12 = TwitterSearchGui.getWordAtIndex(text, 15);

		// Then
		assertThat(actual1).isEqualTo("123");
		assertThat(actual2).isEqualTo("12");
		assertThat(actual3).isEqualTo("1");
		assertThat(actual4).isEqualTo("");
		assertThat(actual5).isEqualTo("Blah");
		assertThat(actual6).isEqualTo("Bla");
		assertThat(actual7).isEqualTo("Bl");
		assertThat(actual8).isEqualTo("B");
		assertThat(actual9).isEqualTo("");

		assertThat(actual10).isEqualTo("");
		assertThat(actual11).isEqualTo("Fooba");
		assertThat(actual12).isEqualTo("Foobar");
	}
}