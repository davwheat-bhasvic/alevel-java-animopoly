package dev.davwheat;

import java.util.Scanner;

public class IOHelper {
    Scanner scanner;

    public IOHelper() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a string until it passes the validation
     *
     * @param message     Message to output before input is read
     * @param failMessage Message to output if validation fails
     * @return string     The inputed string
     */
    public String readString(String message, String failMessage) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine();
            if (validStringInput(input)) {
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
     * @return int        The inputted integer
     */
    public int readInteger(String message, String failMessage) {
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine();
            if (validIntInput(input)) {
                return Integer.parseInt(input);
            }
            System.out.println(failMessage);
        }
    }

    private Boolean validStringInput(String input) {
        if (input.length() > 0) {
            return true;
        }

        return false;
    }

    private Boolean validIntInput(String input) {
        if (input.length() > 0) {
            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }
}