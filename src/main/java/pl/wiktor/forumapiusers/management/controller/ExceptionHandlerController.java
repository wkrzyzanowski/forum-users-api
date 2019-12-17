package pl.wiktor.forumapiusers.management.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.wiktor.forumapiusers.management.model.exceptions.ErrorResponse;
import pl.wiktor.forumapiusers.management.model.exceptions.UserNotFoundException;
import pl.wiktor.forumapiusers.management.model.exceptions.WrongCounterParameterException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> sendUserNotFoundMessage(UserNotFoundException e) {
        String message = MessageFormat.format("USER WITH UUID {0} NOT FOUND.", e.getGuid());
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({WrongCounterParameterException.class})
    public ResponseEntity<ErrorResponse> sendWrongCounterParameter(WrongCounterParameterException e) {
        String message = MessageFormat.format("WRONG PARAMETER. DETAILS: {0}", e.getMessage());
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse(error.getField() + " " + error.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
