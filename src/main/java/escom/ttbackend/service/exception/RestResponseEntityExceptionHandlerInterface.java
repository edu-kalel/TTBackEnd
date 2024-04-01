package escom.ttbackend.service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * An interface that a {@link ControllerAdvice} class implements
 * for handling errors and exceptions.
 * Controller advice classes define handling for common
 * errors and exceptions that might be thrown when doing web
 * requests. They handle all exceptions from all {@link RestController}
 * annotated classes.
 */
public interface RestResponseEntityExceptionHandlerInterface {

    /**
     * Method that receives and handles internal exceptions.
     *
     * @param exception The Exception being handled.
     * @param body      The body for the exception's message.
     * @param headers   The headers of the web request.
     * @param status    The status of the web request.
     * @param request   The web request that threw the exception.
     * @return A {@code ResponseEntity} with the response to the exception.
     */
    ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                   HttpStatus status, WebRequest request);
}
