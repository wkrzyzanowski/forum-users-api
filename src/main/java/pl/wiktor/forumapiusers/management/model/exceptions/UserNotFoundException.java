package pl.wiktor.forumapiusers.management.model.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {

    private String guid;

    public UserNotFoundException(String guid) {
        this.guid = guid;
    }
}
