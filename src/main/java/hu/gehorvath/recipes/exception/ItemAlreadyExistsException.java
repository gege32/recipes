package hu.gehorvath.recipes.exception;

public class ItemAlreadyExistsException extends RuntimeException{

    public ItemAlreadyExistsException(String message) {
        super(message);
    }
}
