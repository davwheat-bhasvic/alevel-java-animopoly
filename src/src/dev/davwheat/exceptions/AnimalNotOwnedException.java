package dev.davwheat.exceptions;

public class AnimalNotOwnedException extends Exception {
    public AnimalNotOwnedException(String errorMessage) {
        super(errorMessage);
    }
}
