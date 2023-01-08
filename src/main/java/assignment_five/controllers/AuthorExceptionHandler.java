package assignment_five.controllers;

import assignment_five.utils.AuthorNotFoundException;
import assignment_five.utils.AuthorValidationException;
import assignment_five.utils.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
public class AuthorExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> response(AuthorNotFoundException e) {
        return ResponseEntity.of(Optional.of(ExceptionResponse.of(e)));
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> response(AuthorValidationException e) {
        return ResponseEntity.of(Optional.of(ExceptionResponse.of(e)));
    }

}
