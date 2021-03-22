package dev.davwheat.exceptions;

public class AnimalAlreadyOwnedException extends Exception {
    public AnimalAlreadyOwnedException(final String errorMessage) {
        super(errorMessage);
    }
}
