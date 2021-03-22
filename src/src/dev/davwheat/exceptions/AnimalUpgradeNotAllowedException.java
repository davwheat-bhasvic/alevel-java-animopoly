package dev.davwheat.exceptions;

public class AnimalUpgradeNotAllowedException extends Exception {
    public AnimalUpgradeNotAllowedException(final String errorMessage) {
        super(errorMessage);
    }
}
