package pl.wiktor.forumapiusers.exception;

import lombok.Getter;

@Getter
public class WrongCounterParameterException extends RuntimeException {

    private String message;

    public WrongCounterParameterException(String s) {
        this.message = s;
    }
}
