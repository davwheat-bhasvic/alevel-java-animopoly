package dev.davwheat.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(final String errorMessage) {
        super(errorMessage);
    }
}
