package ua.training.controller.validator;

import static ua.training.util.ExceptionConstants.EXCEPTION_VALIDATION_INCORRECT_EMAIL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.training.exception.ValidationException;

public class EmailValidator implements Validator {

    private Pattern patternEmail = Pattern.compile("^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    @Override
    public void validate(String email) {
        Matcher matcher = patternEmail.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException().setUserMessage(EXCEPTION_VALIDATION_INCORRECT_EMAIL).addParameter(email);
        }
    }
}
