package pl.tkowalcz.utils;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WordUtilsTest {

	@Test
	public void shouldReturnWord() {
		// Given
		String text = "Blah 123 Foobar";

		// When
		String actual1 = WordUtils.getWordAtIndex(text, 8);
		String actual2 = WordUtils.getWordAtIndex(text, 7);
		String actual3 = WordUtils.getWordAtIndex(text, 6);
		String actual4 = WordUtils.getWordAtIndex(text, 5);
		String actual5 = WordUtils.getWordAtIndex(text, 4);
		String actual6 = WordUtils.getWordAtIndex(text, 3);
		String actual7 = WordUtils.getWordAtIndex(text, 2);
		String actual8 = WordUtils.getWordAtIndex(text, 1);
		String actual9 = WordUtils.getWordAtIndex(text, 0);

		String actual10 = WordUtils.getWordAtIndex(text, 9);
		String actual11 = WordUtils.getWordAtIndex(text, 14);
		String actual12 = WordUtils.getWordAtIndex(text, 15);

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