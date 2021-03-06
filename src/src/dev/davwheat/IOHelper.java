package dev.davwheat;

import java.util.Scanner;
import java.util.function.Function;

/**
 * Class to make inputting data much easier.
 */
public class IOHelper {
    protected Scanner scanner;

    public IOHelper() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Used in tests to allow a custom Scanner to be set.
     * <p>
     * This allows us to provide custom input to the IOHelper functions.
     * <p>
     * <b>Do not use this in normal code.</b>
     *
     * @param scanner custom scanner
     * @implNote This is only used within tests. Please DO NOT use this in normal code.
     */
    public void setScanner(final Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads a string until it passes the validation
     *
     * @param message     Message to output before input is read
     * @param failMessage Message to output if validation fails
     * @return The inputted string
     */
    public String readString(final String message, final String failMessage) {
        while (true) {
            System.out.println(message);
            final String input = this.scanner.nextLine();
            if (this.validStringInput(input)) {
                return input;
            }
            System.out.println(failMessage);
        }
    }

    /**
     * Reads a string until it passes the validation
     *
     * @param message         Message to output before input is read
     * @param failMessage     Message to output if validation fails
     * @param customValidator Custom string validator
     * @return The inputted string
     */
    public String readString(final String message, final String failMessage, final Function<String, Boolean> customValidator) {
        while (true) {
            System.out.println(message);
            final String input = this.scanner.nextLine();
            if (this.validStringInput(input) && customValidator.apply(input)) {
                return input;
            }
            System.out.println(failMessage);
        }
    }

    /**
     * Reads an int until it passes the validation
     *
     * @param message     Message to output before input is read
     * @param failMessage Message to output if validation fails
     * @return The inputted integer
     */
    public int readInteger(final String message, final String failMessage) {
        while (true) {
            System.out.println(message);
            final String input = this.scanner.nextLine();
            if (this.validIntInput(input)) {
                return Integer.parseInt(input);
            }
            System.out.println(failMessage);
        }
    }

    /**
     * Reads an int until it passes the validation
     *
     * @param message         Message to output before input is read
     * @param failMessage     Message to output if validation fails
     * @param customValidator Custom validator
     * @return The inputted integer
     */
    public int readInteger(final String message, final String failMessage, final Function<Integer, Boolean> customValidator) {
        while (true) {
            System.out.println(message);
            final String input = this.scanner.nextLine();
            if (this.validIntInput(input) && customValidator.apply(Integer.parseInt(input))) {
                return Integer.parseInt(input);
            }
            System.out.println(failMessage);
        }
    }

    /**
     * Reads a char until it passes the validation
     *
     * @param message     Message to output before input is read
     * @param failMessage Message to output if validation fails
     * @return The inputted char
     */
    public char readChar(final String message, final String failMessage) {
        while (true) {
            System.out.println(message);
            final String input = this.scanner.nextLine();
            if (this.validCharInput(input)) {
                return (input).charAt(0);
            }
            System.out.println(failMessage);
        }
    }

    /**
     * Reads a char until it passes the validation
     *
     * @param message         Message to output before input is read
     * @param failMessage     Message to output if validation fails
     * @param customValidator Custom char validator
     * @return string     The inputted char
     */
    public char readChar(final String message, final String failMessage, final Function<Character, Boolean> customValidator) {
        while (true) {
            System.out.println(message);
            final String input = this.scanner.nextLine();
            if (this.validCharInput(input) && customValidator.apply(input.charAt(0))) {
                return (input).charAt(0);
            }
            System.out.println(failMessage);
        }
    }

    private boolean validStringInput(final String input) {
        return input.strip().length() > 0;
    }

    private boolean validIntInput(final String input) {
        if (input.length() > 0) {
            try {
                Integer.parseInt(input);
                return true;
            } catch (final NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    private boolean validCharInput(final String input) {
        if (input.length() == 1) {
            return input.matches("[A-Za-z0-9!??%?*]");
        }

        return false;
    }

    /**
     * Pauses execution until the user presses ENTER.
     * <p>
     * Does not display any prompts, so ensure that you inform the user that they should press ENTER before you call this.
     */
    public void pressEnterToContinue() {
        this.scanner.nextLine();
    }

    /**
     * Pre-created custom char validator used to only accept Y/N inputs.
     */
    public static Function<Character, Boolean> YesNoCharValidator = (character -> {
        boolean isYes = character.toString().equalsIgnoreCase("y");
        boolean isNo = character.toString().equalsIgnoreCase("n");

        return isYes || isNo;
    });
}