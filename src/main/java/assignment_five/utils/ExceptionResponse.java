package assignment_five.utils;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String responseMessage;

    private ExceptionResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    public static ExceptionResponse of(AuthorException exception) {
        return new ExceptionResponse(exception.getMessage());
    }
}
