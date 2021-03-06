package pl.wiktor.forumapiusers.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.wiktor.forumapiusers.exception.model.ErrorResponse;
import pl.wiktor.forumapiusers.exception.RoleException;
import pl.wiktor.forumapiusers.exception.UserException;
import pl.wiktor.forumapiusers.exception.WrongCounterParameterException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserException.class})
    public ResponseEntity<Object> handleUserExceptions(UserException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleUserExceptions(BadCredentialsException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonList(new ErrorResponse(message)));
    }

    @ExceptionHandler({RoleException.class})
    public ResponseEntity<Object> handleRoleExceptions(RoleException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({WrongCounterParameterException.class})
    public ResponseEntity<Object> sendWrongCounterParameter(WrongCounterParameterException e) {
        String message = MessageFormat.format("WRONG PARAMETER. DETAILS: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse(error.getField() + " " + error.getDefaultMessage(), error.toString()));
        }
        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}
