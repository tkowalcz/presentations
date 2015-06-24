package pl.tkowalcz.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * This class given text and caret position within the text returns word the caret is positioned inside.
 * <p>
 * Examples will use text <code>Blah 123 Foobar</code> and | will denote caret position.
 * </p>
 * <p>
 * <code>Blah 123| Foobar</code>
 * <code>123</code>
 * <code>Bl|ah 123 Foobar</code>
 * <code>Blah</code>
 * <code>Blah |123 Foobar</code>
 * <code></code>
 * </p>
 */
public class WordUtils {

	public static String getWordAtIndex(String text, Integer position) {
		int start = position;
		for (; start > 0; start--) {
			if (Character.isWhitespace(text.charAt(start - 1))) {
				break;
			}
		}

		if (start == position) {
			return StringUtils.EMPTY;
		}

		return text.substring(start, position);
	}
}
