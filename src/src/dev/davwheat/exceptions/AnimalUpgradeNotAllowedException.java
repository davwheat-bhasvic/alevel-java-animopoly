package dev.davwheat.exceptions;

public class AnimalUpgradeNotAllowedException extends Exception {
    public AnimalUpgradeNotAllowedException(String errorMessage) {
        super(errorMessage);
    }
}
