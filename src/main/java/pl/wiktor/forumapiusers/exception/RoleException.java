package pl.wiktor.forumapiusers.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleException extends RuntimeException {
    public static final String NAME_NOT_FOUND = "Cannot found role with name: {0}";

    private String message;

    private String details;

    public RoleException(String message) {
        this.message = message;
    }

    public RoleException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
