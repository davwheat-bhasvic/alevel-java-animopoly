package dev.davwheat.exceptions;

public class DeckIsLockedException extends Exception {
    public DeckIsLockedException(final String errorMessage) {
        super(errorMessage);
    }
}
