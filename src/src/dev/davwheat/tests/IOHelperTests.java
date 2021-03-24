package dev.davwheat.tests;

import dev.davwheat.IOHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IOHelperTests {
    @Test
    @DisplayName("Correctly allows string input.")
    void readString() {
        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("this is a test"));

        final String str = ioHelper.readString("test input", "fail");

        assertEquals("this is a test", str);
    }

    @Test
    @DisplayName("Correctly allows string input with custom validation.")
    void readStringCustom() {
        // Set up streams to allow monitoring of output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("this is a test\ncorrect"));

        final String str = ioHelper.readString("test input", "fail", s -> s.equals("correct"));

        assertEquals(
                String.format("test input%1$sfail%1$stest input%1$s", System.lineSeparator()),
                outContent.toString()
        );

        assertEquals(
                "correct",
                str
        );

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Does not allow invalid string input.")
    void readInvalidString() {
        // Set up streams to allow monitoring of output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("\ntest"));

        final String str = ioHelper.readString("test input", "fail");

        // We detect the initial message, failure message, then the prompt message again
        assertEquals(
                String.format("test input%1$sfail%1$stest input%1$s", System.lineSeparator()),
                outContent.toString()
        );
        assertEquals(
                "test",
                str
        );

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Correctly allows int input.")
    void readInteger() {
        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("123"));

        final int num = ioHelper.readInteger("test input", "fail");

        assertEquals(123, num);
    }

    @Test
    @DisplayName("Correctly allows int input with custom validation.")
    void readIntegerCustom() {
        // Set up streams to allow monitoring of output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("12\n123"));

        final int num = ioHelper.readInteger("test input", "fail", i -> i == 123);

        assertEquals(
                String.format("test input%1$sfail%1$stest input%1$s", System.lineSeparator()),
                outContent.toString()
        );

        assertEquals(
                123,
                num
        );

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Does not allow invalid int input.")
    void readInvalidInteger() {
        // Set up streams to allow monitoring of output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("abc\n123"));

        final int num = ioHelper.readInteger("test input", "fail");

        assertEquals(
                String.format("test input%1$sfail%1$stest input%1$s", System.lineSeparator()),
                outContent.toString()
        );

        assertEquals(
                123,
                num
        );

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Correctly allows char input.")
    void readChar() {
        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("a"));

        final char chr = ioHelper.readChar("test input", "fail");

        assertEquals('a', chr);
    }

    @Test
    @DisplayName("Correctly allows char input with custom validation.")
    void readCharCustom() {
        // Set up streams to allow monitoring of output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("b\na"));

        final char chr = ioHelper.readChar("test input", "fail", s -> s == 'a');

        assertEquals(
                String.format("test input%1$sfail%1$stest input%1$s", System.lineSeparator()),
                outContent.toString()
        );

        assertEquals(
                'a',
                chr
        );

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Does not allow invalid char input.")
    void readInvalidChar() {
        // Set up streams to allow monitoring of output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        final IOHelper ioHelper = new IOHelper();
        ioHelper.setScanner(new Scanner("\na"));

        final char chr = ioHelper.readChar("test input", "fail");

        // We detect the initial message, failure message, then the prompt message again
        assertEquals(
                String.format("test input%1$sfail%1$stest input%1$s", System.lineSeparator()),
                outContent.toString()
        );
        assertEquals(
                'a',
                chr
        );

        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Correctly requires ENTER to continue.")
    void pressEnterToContinue() {
        assertDoesNotThrow(() -> {
            final IOHelper ioHelper = new IOHelper();
            ioHelper.setScanner(new Scanner("\n"));
            ioHelper.pressEnterToContinue();
        });
    }
}