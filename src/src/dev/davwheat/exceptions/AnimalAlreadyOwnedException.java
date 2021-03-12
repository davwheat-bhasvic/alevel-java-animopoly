package dev.davwheat.exceptions;

public class AnimalAlreadyOwnedException extends Exception {
    public AnimalAlreadyOwnedException(String errorMessage) {
        super(errorMessage);
    }
}
