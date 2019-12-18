package pl.wiktor.forumapiusers.management.model.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserException extends RuntimeException {

    public static final String UUID_NOT_FOUND = "Cannot found user with UUID: {0}";

    private String message;

    private String details;

    public UserException(String message) {
        this.message = message;
    }

    public UserException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
