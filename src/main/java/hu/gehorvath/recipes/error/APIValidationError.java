package hu.gehorvath.recipes.error;

import hu.gehorvath.recipes.exception.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class APIValidationError extends APIError{

    MethodArgumentNotValidException error;

    public APIValidationError(MethodArgumentNotValidException e) {
        super(new ResponseException(e.getMessage(), HttpStatus.BAD_REQUEST));
        this.error = e;
    }



}
