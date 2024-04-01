package escom.ttbackend.service.exception;


import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * {@link ControllerAdvice} class for handling errors and exceptions.
 * Controller advice classes define handling for common
 * errors and exceptions that might be thrown when doing web
 * requests. They handle all exceptions from all {@link RestController}
 * annotated classes.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
        implements RestResponseEntityExceptionHandlerInterface {

    /**
     * Handles {@link UsernameNotFoundException}.
     *
     * @param ex      The RuntimeException being handled.
     * @param request The web request that threw the exception.
     * @return A {@code ResponseEntity} with the response to the exception.
     */
    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleUsernameNotFoundConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles {@link BadCredentialsException}.
     *
     * @param ex      The RuntimeException being handled.
     * @param request The web request that threw the exception.
     * @return A {@code ResponseEntity} with the response to the exception.
     */
    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredentialsConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handles {@link EntityExistsException}.
     *
     * @param ex      The RuntimeException being handled.
     * @param request The web request that threw the exception.
     * @return A {@code ResponseEntity} with the response to the exception.
     */
    @ExceptionHandler(value = {EntityExistsException.class})
    protected ResponseEntity<Object> handleEntityExistsConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {
        ApiError error = new ApiError(status.value(), exception.getMessage());
        return super.handleExceptionInternal(exception, error, headers, status, request);
    }

}
