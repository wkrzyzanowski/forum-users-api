package pl.wiktor.forumapiusers.management.model.exceptions;

public class InsufficientUserDataException extends RuntimeException {

    private String message;

    public InsufficientUserDataException(String s) {
        this.message = s;
    }
}
