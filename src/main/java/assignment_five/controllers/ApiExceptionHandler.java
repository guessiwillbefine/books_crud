package assignment_five.controllers;

import assignment_five.utils.ExceptionResponse;
import assignment_five.utils.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(
            {AuthorNotFoundException.class,
                    BookNotFoundException.class})
    public ResponseEntity<ExceptionResponse> notFoundResponseHandler(RuntimeException e) {
        return ResponseEntity.status(404).body(ExceptionResponse.of(e));
    }

    @ExceptionHandler({AuthorDuplicateException.class, AuthorValidationException.class,
            BookDuplicateException.class, BookValidationException.class})
    public ResponseEntity<ExceptionResponse> badRequestResponseHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
    }
}
