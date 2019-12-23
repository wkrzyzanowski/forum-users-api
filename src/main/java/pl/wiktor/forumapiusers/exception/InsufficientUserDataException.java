package pl.wiktor.forumapiusers.exception;

public class InsufficientUserDataException extends RuntimeException {

    private String message;

    public InsufficientUserDataException(String s) {
        this.message = s;
    }
}
