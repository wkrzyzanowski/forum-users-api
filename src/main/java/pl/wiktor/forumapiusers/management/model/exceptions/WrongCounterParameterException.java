package pl.wiktor.forumapiusers.management.model.exceptions;

import lombok.Getter;

@Getter
public class WrongCounterParameterException extends RuntimeException {

    private String message;

    public WrongCounterParameterException(String s) {
        this.message = s;
    }
}
