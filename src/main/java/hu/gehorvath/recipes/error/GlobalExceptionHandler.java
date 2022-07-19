package hu.gehorvath.recipes.error;

import hu.gehorvath.recipes.exception.InternalErrorException;
import hu.gehorvath.recipes.exception.ItemAlreadyExistsException;
import hu.gehorvath.recipes.exception.ItemDoesNotExistException;
import hu.gehorvath.recipes.exception.ResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ResponseException.class)
    public ResponseEntity<Object> handleResponseException(ResponseException exception, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        APIError error = new APIError(exception);
        error.setMessage(exception.getMessage());

        logger.error("ResponseException handled: " + error, exception);
        return handleExceptionInternal(exception, error, headers, exception.getStatus(), request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        APIValidationError error = new APIValidationError(ex);
        error.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception exception, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        APIError error = new APIError(new InternalErrorException(exception.getMessage(), exception));
        error.setMessage(exception.getMessage());
        return handleExceptionInternal(exception, error, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = ItemDoesNotExistException.class)
    public ResponseEntity<Object> handleItemDoesNotExistsException(ItemDoesNotExistException exception,
                                                                        WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        APIError error = new APIError(new ResponseException(exception.getMessage(), HttpStatus.NOT_FOUND));
        error.setMessage(exception.getMessage());
        return handleExceptionInternal(exception, error, headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = ItemAlreadyExistsException.class)
    public ResponseEntity<Object> handleItemAlreadyExistsException(ItemAlreadyExistsException exception,
                                                                        WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        APIError error = new APIError(new ResponseException(exception.getMessage(), HttpStatus.BAD_REQUEST));
        error.setMessage(exception.getMessage());
        return handleExceptionInternal(exception, error, headers, HttpStatus.BAD_REQUEST, request);
    }

}
