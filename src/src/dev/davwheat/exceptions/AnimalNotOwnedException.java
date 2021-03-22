package dev.davwheat.exceptions;

public class AnimalNotOwnedException extends Exception {
    public AnimalNotOwnedException(final String errorMessage) {
        super(errorMessage);
    }
}
