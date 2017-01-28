package ua.training.exception;

public class ApplicationException extends RuntimeException {

    private String userMessage;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public String getUserMessage() {
        return userMessage;
    }

    public ApplicationException setUserMessage(String userMessage) {
        this.userMessage = userMessage;
        return this;
    }

    public boolean isUserMessage() {
        return (userMessage != null);
    }
}
