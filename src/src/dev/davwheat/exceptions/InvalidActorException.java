package dev.davwheat.exceptions;

public class InvalidActorException extends Exception {
    public InvalidActorException(final String errorMessage) {
        super(errorMessage);
    }
}
