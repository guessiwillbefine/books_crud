package assignment_five.utils.exceptions;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class BookValidationException extends BookException {
    public BookValidationException(String msg) {
        super(msg);
    }
    public BookValidationException(BindingResult bindingResult) {
        super(bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + " -> " + error.getDefaultMessage())
                .collect(Collectors.joining(", ","[","]")));
    }
}
