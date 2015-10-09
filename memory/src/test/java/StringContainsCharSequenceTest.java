/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/**
 * @test
 * @summary Test cases for String::contains with argument of type CharSequence.
 * @run testng StringContainsCharSequenceTest
 * @author Tomasz Kowalczewski
 */

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test(groups = {"unit", "string", "lang", "libs"})
public class StringContainsCharSequenceTest {

    @Test
    public void nonEmptyStringShouldContainEmptyCharSequence() {
        // Given
        String nonemptyString = "Not empty";
        CharSequence emptyCharSequence = new StringBuilder("");

        // When
        boolean containsEmptyCharSequence = nonemptyString.contains(emptyCharSequence);

        // Then
        assertTrue(containsEmptyCharSequence);
    }

    @Test
    public void emptyStringhouldContainEmptyCharSequence() {
        // Given
        String emptyString = "";
        CharSequence emptyCharSequence = new StringBuilder("");

        // When
        boolean containsEmptyCharSequence = emptyString.contains(emptyCharSequence);

        // Then
        assertTrue(containsEmptyCharSequence);
    }

    @Test
    public void emptyStringhouldNotContainNonEmptyCharSequence() {
        // Given
        String emptyString = "";
        CharSequence charSequence = new StringBuilder("Not empty");

        // When
        boolean containsCharSequence = emptyString.contains(charSequence);

        // Then
        assertFalse(containsCharSequence);
    }

    @Test
    public void shouldContainCharSequence() {
        // Given
        String string = "Not an empty string";
        CharSequence charSequence = new StringBuilder("an empty");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsCharSequence);
    }

    @Test
    public void shouldNotContainCharSequence() {
        // Given
        String string = "Not an empty string";
        CharSequence charSequence = new StringBuilder("some text");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertFalse(containsCharSequence);
    }

    @Test
    public void shouldFindMatchAfterSeveralPartialMatches() {
        // Given
        String string = " ABAABBABcA";
        CharSequence charSequence = new StringBuilder("ABc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsCharSequence);
    }

    @Test
    public void shouldSkipPartialMatchesIfNoMatchPresent() {
        // Given
        String string = " ABAABBAB";
        CharSequence charSequence = new StringBuilder("ABc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertFalse(containsCharSequence);
    }

    @Test
    public void shouldHandleInputLongerThanStringItself() {
        // Given
        String string = "ABc";
        CharSequence charSequence = new StringBuilder("ABcc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertFalse(containsCharSequence);
    }

    @Test
    public void shouldFindSubstringAtTheEndOfTheString() {
        // Given
        String string = "123ABc";
        CharSequence charSequence = new StringBuilder("ABc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsCharSequence);
    }

    // Test main loop conditional. We have a partial match so we break out of the nested loop
    // at not matching character. Then apply main loop test. Here we verify that if matching
    // string is at the end we still enter the loop for last final match attempt.
    @Test
    public void shouldFindSubstringAtTheEndOfTheStringAfterOneCharacterPartialMatch() {
        // Given
        String string = "AABc";
        CharSequence charSequence = new StringBuilder("ABc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsCharSequence);
    }

    @Test
    public void shouldFindSubstringAtTheStartOfTheString() {
        // Given
        String string = "123ABc";
        CharSequence charSequence = new StringBuilder("123");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsCharSequence);
    }

    @Test
    public void shouldFindSubstringEqualToTheString() {
        // Given
        String string = "123ABc";
        CharSequence charSequence = new StringBuilder("123ABc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsCharSequence);
    }

    // This verifies break condition in loop that searches for occurrence of
    // first matching character. If we break too soon, second loop assumes we
    // found first matching character and reports invalid match.
    @Test
    public void shouldNotFindSubstringWithMismatchedFirstCharacterAtTheEndOfThString() {
        // Given
        String string = "123ABc";
        CharSequence charSequence = new StringBuilder("cBc");

        // When
        boolean containsCharSequence = string.contains(charSequence);

        // Then
        assertFalse(containsCharSequence);
    }

    @Test
    public void shouldHandleStringArgumentCorrectly() {
        // Given
        String string = "Not an empty string";
        CharSequence charSequence = "an empty";

        // When
        boolean containsEmptyCharSequence = string.contains(charSequence);

        // Then
        assertTrue(containsEmptyCharSequence);
    }
}
