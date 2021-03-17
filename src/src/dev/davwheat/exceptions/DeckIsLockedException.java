package dev.davwheat.exceptions;

public class DeckIsLockedException extends Exception {
    public DeckIsLockedException(String errorMessage) {
        super(errorMessage);
    }
}
