package pl.tkowalcz.utils;

import org.apache.commons.lang3.StringUtils;

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
