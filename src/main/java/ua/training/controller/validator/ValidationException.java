package ua.training.controller.validator;

import ua.training.exception.ApplicationException;

public class ValidationException extends ApplicationException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
