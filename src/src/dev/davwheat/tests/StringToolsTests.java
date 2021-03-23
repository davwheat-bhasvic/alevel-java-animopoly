package dev.davwheat.tests;

import dev.davwheat.StringTools;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("StringTools")
class StringToolsTests {

    @Test
    @DisplayName("Centres a list of Strings with a supplied width.")
    void correctlyCentresLinesOfText() {
        assertEquals(
                Arrays.asList("this is text", "   line of  ", "  text that ", "  should be ", "  centred!  "),
                StringTools.centreText(Arrays.asList("this is text", "line of", "text that", "should be", "centred!"), 12)
        );
    }

    @Test
    @DisplayName("Splits a String with a supplied max length, favouring whitespace.")
    void correctlySplitsStringsAtWhitespace() {
        assertEquals(
                Arrays.asList("this is a", "line of", "text that", "should be", "wrapped"),
                StringTools.splitStringAtWhitespace("this is a line of text that should be wrapped", 10)
        );
    }

    @Test
    @DisplayName("Concatenates a list of Strings.")
    void correctlyConcatenatesListOfLines() {
        assertEquals(
                "these are lines\nof text that should be\ncombined like lines\nof text",
                StringTools.concatenateListOfLines(
                        Arrays.asList("these are lines", "of text that should be", "combined like lines", "of text")
                )
        );
    }

    @Test
    @DisplayName("Concatenates a Strings list with leading/trailing whitespace.")
    void correctlyConcatenatesListOfLinesWithWhitespace() {
        assertEquals(
                "these are lines \nof text that should be\n combined like lines\n of text ",
                StringTools.concatenateListOfLines(
                        Arrays.asList("these are lines ", "of text that should be", " combined like lines", " of text ")
                )
        );
    }
}