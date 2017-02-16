package ua.training.controller.validator;

import ua.training.exception.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator {

    private static final String EXCEPTION_INCORRECT_EMAIL
            = "exception.validation.email";
    private Pattern patternEmail
            = Pattern.compile("^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    @Override
    public void validate(String email) {
        Matcher matcher = patternEmail.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_EMAIL)
                    .addParameter(email);
        }
    }
}
