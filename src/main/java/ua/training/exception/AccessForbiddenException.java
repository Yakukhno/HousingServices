package ua.training.exception;

public class AccessForbiddenException extends ApplicationException {

    private static final String EXCEPTION_ACCESS_IS_FORBIDDEN_FOUND = "Access is forbidden";

    public AccessForbiddenException() {
        super(EXCEPTION_ACCESS_IS_FORBIDDEN_FOUND);
    }

    public String getMessage() {
        return EXCEPTION_ACCESS_IS_FORBIDDEN_FOUND;
    }
}
