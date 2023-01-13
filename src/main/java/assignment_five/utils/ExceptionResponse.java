package assignment_five.utils;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String response;
    private ExceptionResponse(String responseMessage) {
        this.response = responseMessage;
    }
    public static ExceptionResponse of(RuntimeException exception) {
        return new ExceptionResponse(exception.getMessage());
    }
}
