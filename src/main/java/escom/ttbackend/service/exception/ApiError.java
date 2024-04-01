package escom.ttbackend.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A simple POJO for a web request-thrown exception or error.
 */
@AllArgsConstructor
@Getter
public class ApiError {

    /**
     * The status of the web request this error was thrown by.
     */
    private final int status;

    /**
     * The message of the web request this error was thrown by.
     */
    private final String message;
}
