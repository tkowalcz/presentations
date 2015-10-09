///**
// * @test
// * @summary Test cases for String::indexOf with argument of type CharSequence.
// * To test this method more thoroughly each test is executed three times
// * with different offsets into the source and target arrays. Each time we
// * prepend some 'garbage' to the test string and char sequence, then apply
// * offsets so that indexOf will skip it.
// * @run testng StringIndexOfCharSequenceTest
// * @author Tomasz Kowalczewski
// */
//
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import static org.testng.Assert.assertEquals;
//
//@Test(groups = {"unit", "string", "lang", "libs"})
//public class StringIndexOfCharSequenceTest {
//
//    @DataProvider
//    public static Object[][] stringAndCharSequenceOffsets() {
//        return new Object[][]{
//                {0, 0},
//                {0, 7},
//                {7, 0}
//        };
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void emptyCharSequenceHasIndexOfZero(int stringOffset, int unused) {
//        // Given
//        char[] nonemptyString = stringWithOffset("Not empty", stringOffset);
//        CharSequence emptyCharSequence = "";
//
//        // When
//        int actual = String.indexOf(
//                nonemptyString, stringOffset, nonemptyString.length - stringOffset,
//                emptyCharSequence, 0, emptyCharSequence.length(), 0);
//
//        // Then
//        assertEquals(actual, 0);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void emptyStringShouldNotContainNonEmptyCharSequence(int unused, int charSequenceOffset) {
//        // Given
//        char[] emptyString = "".toCharArray();
//        CharSequence charSequence = charSequenceWithOffset("Not empty", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                emptyString, 0, emptyString.length,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//
//        // Then
//        assertEquals(actual, -1);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldFindSubCharSequence(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("Not an empty string", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("an empty", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, 4);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldNotFindSubCharSequence(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("Not an empty string", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("some text", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, -1);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldFindMatchAfterSeveralPartialMatches(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset(" ABAABBABcA", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("ABc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, 7);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldSkipPartialMatchesIfNoMatchPresent(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset(" ABAABBAB", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("ABc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//
//        // Then
//        assertEquals(actual, -1);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldHandleInputLongerThanStringItself(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("ABc", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("ABcc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, -1);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldFindSubstringAtTheEndOfTheString(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("123ABc", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("ABc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, 3);
//    }
//
//    // Test main loop conditional. We have a partial match so we break out of the nested loop
//    // at not matching character. Then apply main loop test. Here we verify that if matching
//    // string is at the end we still enter the loop for last final match attempt.
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldFindSubstringAtTheEndOfTheStringAfterOneCharacterPartialMatch(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("AABc", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("ABc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, 1);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldFindSubstringAtTheStartOfTheString(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("123ABc", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("123", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, 0);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldFindSubstringEqualToTheString(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("123ABc", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("123ABc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, 0);
//    }
//
//    // This verifies break condition in loop that searches for occurrence of
//    // first matching character. If we break too soon, second loop assumes we
//    // found first matching character and reports invalid match.
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldNotFindSubstringWithMismatchedFirstCharacterAtTheEndOfTheString(int stringOffset, int charSequenceOffset) {
//        // Given
//        char[] string = stringWithOffset("123ABc", stringOffset);
//        CharSequence charSequence = charSequenceWithOffset("cBc", charSequenceOffset);
//
//        // When
//        int actual = String.indexOf(
//                string, stringOffset, string.length - stringOffset,
//                charSequence, charSequenceOffset, charSequence.length() - charSequenceOffset, 0);
//
//        // Then
//        assertEquals(actual, -1);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldStartMatchingFromIndex(int stringOffset, int unused) {
//        // Given
//        char[] nonemptyString = stringWithOffset("string string", stringOffset);
//        CharSequence charSequence = "str";
//
//        // When
//        int actual = String.indexOf(
//                nonemptyString, stringOffset, nonemptyString.length - stringOffset,
//                charSequence, 0, charSequence.length(), 4);
//
//        // Then
//        assertEquals(actual, 7);
//    }
//
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldAcceptFromIndexBeingBelowZero(int stringOffset, int unused) {
//        // Given
//        char[] nonemptyString = stringWithOffset("Not empty", stringOffset);
//        CharSequence charSequence = "em";
//
//        // When
//        int actual = String.indexOf(
//                nonemptyString, stringOffset, nonemptyString.length - stringOffset,
//                charSequence, 0, charSequence.length(), -10);
//
//        // Then
//        assertEquals(actual, 4);
//    }
//
//    @Test(dataProvider = "stringAndCharSequenceOffsets")
//    public void shouldAcceptFromIndexBeingEqualToStringOffset(int stringOffset, int unused) {
//        // Given
//        char[] nonemptyString = stringWithOffset("Not empty", stringOffset);
//        CharSequence charSequence = "no";
//
//        int searchedStringSize = nonemptyString.length - stringOffset;
//
//        // When
//        int actual = String.indexOf(
//                nonemptyString, stringOffset, searchedStringSize,
//                charSequence, 0, charSequence.length(), searchedStringSize);
//
//        // Then
//        assertEquals(actual, -1);
//    }
//
//    private CharSequence charSequenceWithOffset(String string, int charSequenceOffset) {
//        StringBuilder result = new StringBuilder(string.length() + charSequenceOffset);
//        for (int i = 0; i < charSequenceOffset; i++) {
//            result.append(i % 10);
//        }
//
//        result.append(string);
//        return result;
//    }
//
//    private char[] stringWithOffset(String string, int stringOffset) {
//        return charSequenceWithOffset(string, stringOffset).toString().toCharArray();
//    }
//}
