package ua.training.exception;

public class ResourceNotFoundException extends ApplicationException {

    private static final String EXCEPTION_RESOURCE_NOT_FOUND = "Resource not found";

    public ResourceNotFoundException() {
        super(EXCEPTION_RESOURCE_NOT_FOUND);
    }

    public String getMessage() {
        return EXCEPTION_RESOURCE_NOT_FOUND;
    }
}
