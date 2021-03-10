package dev.davwheat.exceptions;

public class InvalidActorException extends Exception {
    public InvalidActorException(String errorMessage) {
        super(errorMessage);
    }
}
