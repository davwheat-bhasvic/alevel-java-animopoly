package dev.davwheat;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

/**
 * A collection of utilities for modifying Strings.
 */
public class StringTools {
    /**
     * Takes a list of strings as input, and a maximum length, then centres the text within that length.
     * <p>
     * If there is an odd amount of spacing required, we favour extra spacing on the left.
     *
     * @param text  input string to be centred
     * @param width the maximum length per line
     * @return centred, possibly multi-lined, string
     */
    public static ArrayList<String> centreText(final List<String> text, final int width) {
        final ArrayList<String> newList = new ArrayList<>();

        text.forEach(s -> {
            newList.add(StringTools.centreText(s, width));
        });

        return newList;
    }

    /**
     * Takes a string as input, and a maximum length, then centres the text within that length.
     * <p>
     * If there is an odd amount of spacing required, we favour extra spacing on the left.
     * <p>
     * If the input is longer than the width, nothing will be done.
     *
     * @param text  input string to be centred
     * @param width the maximum length per line
     * @return centred string
     */
    public static String centreText(final String text, final int width) {
        final int charsToFill = width - text.length();
        final int charsLeft = (int) Math.ceil(charsToFill / 2d);
        final int charsRight = (int) Math.floor(charsToFill / 2d);

        return String.format("%s%s%s", " ".repeat(charsLeft), text, " ".repeat(charsRight));
    }

    /**
     * Joins an ArrayList of Strings into a single string, separated by new lines.
     * <p>
     * Preserves all whitespace on each line.
     *
     * @param lines list of strings
     * @return new-line delimited string
     */
    public static String concatenateListOfLines(final List<String> lines) {
        return String.join("\n", lines);
    }

    /**
     * Takes a string, and wraps it to the maximum length provided, while adhering to
     * whitespace location where possible.
     *
     * @param text  Input
     * @param width Maximum length per line
     * @return Wrapped string
     */
    public static List<String> splitStringAtWhitespace(String text, final int width) {
        final ArrayList<String> list = new ArrayList<>();

        while (text.length() >= width) {
            final String widthStr = text.substring(0, width);

            if (widthStr.endsWith(" ") || text.charAt(width) == ' ') {
                // It's the end of a word! Woohoo!
                list.add(widthStr.trim());
                text = text.substring(width).trim();
            } else {
                int splitIndex = widthStr.lastIndexOf(' ');
                if (splitIndex == -1) splitIndex = width;

                list.add(widthStr.substring(0, splitIndex));
                text = text.substring(splitIndex).trim();
            }
        }

        list.add(text.trim());

        return list;
    }
}
