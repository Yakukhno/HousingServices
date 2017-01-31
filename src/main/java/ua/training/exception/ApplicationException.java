package ua.training.exception;

import java.util.ArrayList;
import java.util.List;

public class ApplicationException extends RuntimeException {

    private List<String> parameters = new ArrayList<>();

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

    public ApplicationException addParameter(String parameter) {
        parameters.add(parameter);
        return this;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
