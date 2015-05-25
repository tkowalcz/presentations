package openjdk.java.lang;

public class Strings {

    private final char value[];

    public Strings(String s) {
        value = s.toCharArray();
    }

    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values.
     *
     * @param s the sequence to search for
     * @return true if this string contains {@code s}, false otherwise
     * @since 1.5
     */
//    public boolean contains2(CharSequence s) {
//        if (s.length() == 0) {
//            return true;
//        }
//
//        if (value.length == 0) {
//            return false;
//        }
//
//        char first = s.charAt(0);
//        int max = value.length - s.length();
//
//        for (int i = 0; i <= max; i++) {
//            /* Look for first character. */
//            if (value[i] != first) {
//                while (++i <= max && value[i] != first) {
//                    ;
//                }
//            }
//
//            /* Found first character, now look at the rest of v2 */
//            if (i <= max) {
//                int j = i + 1;
//                int end = j + s.length() - 1;
//                for (int k = 1; j < end && value[j]
//                        == s.charAt(k); j++, k++) {
//                    ;
//                }
//
//                if (j == end) {
//                    /* Found whole string. */
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public boolean contains(String s) {
        return false;
    }

    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values.
     *
     * @param s the sequence to search for
     * @return true if this string contains {@code s}, false otherwise
     * @since 1.5
     */
    public boolean contains(CharSequence s) {
        if (s instanceof String) {
            return contains((String) s);
        }

        return indexOf(value, 0, value.length, s, 0, s.length(), 0) >= 0;
    }

    /**
     * The source is the character array being searched, and the target
     * is the string being searched for. See
     * {@link String#indexOf(char[], int, int, char[], int, int, int)}.
     *
     * @param source       the characters being searched.
     * @param sourceOffset offset of the source string.
     * @param sourceCount  count of the source string.
     * @param target       the characters being searched for.
     * @param targetOffset offset of the target string.
     * @param targetCount  count of the target string.
     * @param fromIndex    the index to begin searching from.
     */
    public static int indexOf(char[] source, int sourceOffset, int sourceCount,
                              CharSequence target, int targetOffset, int targetCount,
                              int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target.charAt(targetOffset);
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first) {
                    ;
                }
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target.charAt(k); j++, k++) {
                    ;
                }

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }
}
